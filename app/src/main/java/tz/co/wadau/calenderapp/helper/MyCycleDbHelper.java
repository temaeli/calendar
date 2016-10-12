package tz.co.wadau.calenderapp.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tz.co.wadau.calenderapp.helper.MyCycleContract.CategoryEntry;
import tz.co.wadau.calenderapp.helper.MyCycleContract.EventEntry;
import tz.co.wadau.calenderapp.models.MCEvent;

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
        Log.d("some", "Creating database");
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
        return eventId;
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
        return event;
    }

    public List<MCEvent> getAllEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<MCEvent> events = new ArrayList<>();

        final String SQL_SELECT_ALL_EVENTS = "SELECT * FROM " + EventEntry.TABLE_NAME;
        Log.e(TAG, SQL_SELECT_ALL_EVENTS);

        Cursor c = db.rawQuery(SQL_SELECT_ALL_EVENTS, null);

        //Loop through all events and add to list
        if(c.moveToFirst()){
            do{
                MCEvent event = new MCEvent();
                event.setId(c.getInt(c.getColumnIndex(EventEntry._ID)));
                event.setDate(c.getString(c.getColumnIndex(EventEntry.COLUMN_EVENT_DATE)));
                event.setColor(c.getString(c.getColumnIndex(EventEntry.COLUMN_EVENT_COLOR)));

                //Add event to the list
                events.add(event);
            }while (c.moveToNext());
        }

        return events;
    }

    public int updateEvent(MCEvent event){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventEntry.COLUMN_EVENT_DATE, event.getDate());
        values.put(EventEntry.COLUMN_EVENT_COLOR, event.getColor());

        //Update row
        return db.update(EventEntry.TABLE_NAME, values, EventEntry._ID + " =?",
                new String[] {String.valueOf(event.getId())});
    }

    public void deleteEvent(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(EventEntry.TABLE_NAME, EventEntry._ID + " =?",
                new String[] {String.valueOf(id)});
    }

    public void deleteAllEvents(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(EventEntry.TABLE_NAME, null, null);
    }

    //Closing connection
    public void closeDb(){
        SQLiteDatabase db = this.getReadableDatabase();
        if(db != null && db.isOpen()){
            db.close();
        }
    }
}
