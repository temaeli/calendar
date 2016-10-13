package tz.co.wadau.calenderapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tz.co.wadau.calenderapp.AlarmNotification;
import tz.co.wadau.calenderapp.InitialSettingsActivity;
import tz.co.wadau.calenderapp.SettingsFragment;
import tz.co.wadau.calenderapp.customviews.DatePreference;
import tz.co.wadau.calenderapp.customviews.MCUtils;
import tz.co.wadau.calenderapp.helper.MyCycleContract.CategoryEntry;
import tz.co.wadau.calenderapp.helper.MyCycleContract.EventEntry;
import tz.co.wadau.calenderapp.models.MCEvent;

import static tz.co.wadau.calenderapp.CalendarActivity.setCycleStatus;
import static tz.co.wadau.calenderapp.customviews.MCUtils.getTimeInMills;

public class MyCycleDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MyCycleDbHelper.class.getSimpleName();

    //Database name
    private static final String DATABASE_NAME = "mycycle.db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " ( "
            + EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EventEntry.COLUMN_EVENT_DATE + " VARCHAR(255) NOT NULL, "
            + EventEntry.COLUMN_EVENT_COLOR + " VARCHAR(255) NOT NULL "
            + ");";

    final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " ( "
            + CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CategoryEntry.COLUMN_CATEGORY_NAME + " VARCHAR(255) NOT NULL "
            + ");";

    public MyCycleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database");
        db.execSQL(SQL_CREATE_EVENTS_TABLE);
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);

        // create new tables
        onCreate(db);
    }

   /*Creating an MCEvent*/

    public long createEvent(MCEvent event) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventEntry.COLUMN_EVENT_DATE, event.getDate());
        values.put(EventEntry.COLUMN_EVENT_COLOR, event.getColor());

        long eventId = db.insert(EventEntry.TABLE_NAME, null, values);
        db.close();
        return eventId;
    }

    public List<Long> createEvents(List<MCEvent> events) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        MCEvent event;
        List<Long> eventIds = new ArrayList<>();

        db.beginTransaction();
        try {

            for(int i=0; i < events.size(); i++){
                event = events.get(i);
                values.put(EventEntry.COLUMN_EVENT_DATE, event.getDate());
                values.put(EventEntry.COLUMN_EVENT_COLOR, event.getColor());
                eventIds.add(db.insert(EventEntry.TABLE_NAME, null, values));
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();

        return eventIds;
    }


    public MCEvent getEvent(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        final String SQL_SELECT_EVENT = "SELECT * FROM " + EventEntry.TABLE_NAME
                + " WHERE " + EventEntry._ID + " = " + id;

        Log.e(TAG, SQL_SELECT_EVENT);

        Cursor c = db.rawQuery(SQL_SELECT_EVENT, null);
        if (c != null) c.moveToFirst();
        MCEvent event = new MCEvent();
        event.setId(c.getInt(c.getColumnIndex(EventEntry._ID)));
        event.setDate(c.getString(c.getColumnIndex(EventEntry.COLUMN_EVENT_DATE)));
        event.setColor(c.getString(c.getColumnIndex(EventEntry.COLUMN_EVENT_COLOR)));
        db.close();
        return event;
    }

    public List<MCEvent> getAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<MCEvent> events = new ArrayList<>();

        final String SQL_SELECT_ALL_EVENTS = "SELECT * FROM " + EventEntry.TABLE_NAME;
        Log.e(TAG, SQL_SELECT_ALL_EVENTS);

        Cursor c = db.rawQuery(SQL_SELECT_ALL_EVENTS, null);

        //Loop through all events and add to list
        if (c.moveToFirst()) {
            do {
                MCEvent event = new MCEvent();
                event.setId(c.getInt(c.getColumnIndex(EventEntry._ID)));
                event.setDate(c.getString(c.getColumnIndex(EventEntry.COLUMN_EVENT_DATE)));
                event.setColor(c.getString(c.getColumnIndex(EventEntry.COLUMN_EVENT_COLOR)));

                //Add event to the list
                events.add(event);
            } while (c.moveToNext());
        }
        db.close();

        return events;
    }

    public int updateEvent(MCEvent event) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventEntry.COLUMN_EVENT_DATE, event.getDate());
        values.put(EventEntry.COLUMN_EVENT_COLOR, event.getColor());

        //Update row
        int numRows = db.update(EventEntry.TABLE_NAME, values, EventEntry._ID + " =?",
                new String[]{String.valueOf(event.getId())});
        db.close();
        return numRows;
    }

    public void deleteEvent(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(EventEntry.TABLE_NAME, EventEntry._ID + " =?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllEvents() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(EventEntry.TABLE_NAME, null, null);
        db.close();
    }

    //Closing connection
    public void closeDb() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    //Adding ovulation and mens days
    public static void addMensCycleDays(Context context) {

        MyCycleDbHelper dbHelper = new MyCycleDbHelper(context);
        List<MCEvent> events = new ArrayList<>();

        //Reading user settings then adding menstrual and ovulation days to calendar
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int mensDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_MENS_DAYS, 3);
        int cycleDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_CYCLE_DAYS, 28);
        String lastMonthMensDate = sharedPrefs.getString(SettingsFragment.KEY_PREF_LAST_MONTH_MENS_DATE, "2016-05-21");
        int ovulationDays = 5;
        int luteralPhaseDays = 14;
        boolean cycleCreated = sharedPrefs.getBoolean(InitialSettingsActivity.IS_CYCLE_CREATED, true);
        int daysBeforeFertilityWindow = cycleDays - (luteralPhaseDays + 3);
        int calendarYear = DatePreference.getYear(lastMonthMensDate);
        int calendarMonth = DatePreference.getMonth(lastMonthMensDate) - 1;
        int calendarDay = DatePreference.getDate(lastMonthMensDate);
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(calendarYear, calendarMonth, calendarDay, 0, 0, 1);

        if (!cycleCreated) {
            AlarmNotification alarmNotification = new AlarmNotification();
            alarmNotification.setAlarm(context);
            setCycleStatus(context, true);
        }

        //Add mens days to the database for 2 years
        for (Integer k = 0; k <= 24; k++) {

            for (Integer i = 0; i < mensDays; i++) {
//                compactCalendarView.addEvent(new Event(Color.argb(255, 235, 147, 147), cal.getTimeInMillis()), false);

                events.add(new MCEvent(MCUtils.formatDate(cal.getTime()), "#EB9393"));
                cal.add(Calendar.DATE, 1);
            }

            //Add ovulating days to the database for 2 years
            cal.add(Calendar.DATE, -mensDays);
            cal.add(Calendar.DATE, daysBeforeFertilityWindow);

            for (Integer j = 0; j < ovulationDays; j++) {
//                compactCalendarView.addEvent(new Event(Color.argb(140, 0, 138, 230), cal.getTimeInMillis()), false);
                events.add(new MCEvent(MCUtils.formatDate(cal.getTime()), "#8C008AE6"));
                cal.add(Calendar.DATE, 1);
            }

            cal.add(Calendar.DATE, -(daysBeforeFertilityWindow + ovulationDays)); //Reset calender day to the previous month first mens day
            cal.add(Calendar.DATE, cycleDays);
        }

        dbHelper.deleteAllEvents();
        dbHelper.createEvents(events);
        dbHelper.closeDb();
    }

    public List<Event> getMensCycleDays(Context context) throws ParseException {
        MyCycleDbHelper db = new MyCycleDbHelper(context);
        List mcEvents = db.getAllEvents();
        List events = new ArrayList<>();
        MCEvent e;
        int color;
        long date;

        for (int i=0; i<mcEvents.size(); i++){
            e = (MCEvent) mcEvents.get(i);
            color = Color.parseColor(e.getColor());
            date = getTimeInMills(e.getDate());
            events.add(new Event(color, date));
        }
        return  events;
    }
}
