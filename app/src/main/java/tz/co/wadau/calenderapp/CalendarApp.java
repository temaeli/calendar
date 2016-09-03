package tz.co.wadau.calenderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarApp extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    public static CompactCalendarView compactCalendarView;
    static Calendar cal;
    int[] colorKeyImage = {R.drawable.ic_color_key_red_24dp, R.drawable.ic_color_key_blue_24dp};
    String[] colorKeyDescription = {"Period days", "Ovulation days (Fertility window)"};
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_app);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        // Setting default toolbar title to empty
        actionBar.setTitle(null);

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.drawSmallIndicatorForEvents(false);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        //set initial title
        actionBar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // Changes toolbar title on monthChange
                actionBar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
            }

        });

        //Reading user settings then adding mentral and ovulation days to calendar
        addMensCycleDays(getApplicationContext());

        //Adding key for mentral cycle colors
        CustomListAdapter adapter = new CustomListAdapter(this, colorKeyImage, colorKeyDescription);
        ListView colorKeyList = (ListView) findViewById(R.id.color_key_list);
        colorKeyList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                showSettings();
                return true;
            case R.id.action_today:
                gotoToday();
                actionBar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Adding ovulation and mens days
    public static void addMensCycleDays(Context context) {

        //Reading user settings then adding mentral and ovulation days to calendar
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int mensDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_MENS_DAYS, 3);
        int cycleDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_CYCLE_DAYS, 28);
        String lastMonthMensDate = sharedPrefs.getString(SettingsFragment.KEY_PREF_LAST_MONTH_MENS_DATE, "2016-05-21");
        Calendar todayCalendar = Calendar.getInstance(Locale.getDefault());
        int ovulationDays = 5;
        long notifyBeforePeriod = sharedPrefs.getInt(SettingsFragment.KEY_PREF_PERIOD_NOTIFY_BEFORE_DAYS, 1); //Days to notifiy before period
        boolean isPeriodNotificationEnabled = sharedPrefs.getBoolean(SettingsFragment.KEY_PREF_PERIOD_NOTIFICATIONS, true);

        int daysBeforeOvulation;
        int calendarYear = DatePreference.getYear(lastMonthMensDate);
        int calendarMonth = DatePreference.getMonth(lastMonthMensDate) - 1;
        int calendarDay = DatePreference.getDate(lastMonthMensDate);
        long intervalMillis = 24 * 60 * 60 * 1000 * (cycleDays - notifyBeforePeriod);

        boolean cycleCreated = sharedPrefs.getBoolean(InitialSettingsActivity.IS_CYCLE_CREATED, true);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        cal = Calendar.getInstance(Locale.getDefault());
        cal.set(calendarYear, calendarMonth, calendarDay, 0, 0, 1);

        long dateDiff = (todayCalendar.getTimeInMillis() - cal.getTimeInMillis()) / (24 * 60 * 60 * 1000);
        long alarmIn = (cycleDays - (dateDiff % cycleDays)) - notifyBeforePeriod;

        if (!cycleCreated && isPeriodNotificationEnabled) {
            //Creating notification for the period day
            Calendar nextAlarm = Calendar.getInstance();

            if(alarmIn >= 0){
                //Notification day has not passed yet
                nextAlarm.add(Calendar.DAY_OF_MONTH, (int) alarmIn);
            }else {
                // If notification day has already passed. Set to notifiy next period
                nextAlarm = cal;
                nextAlarm.add(Calendar.DAY_OF_MONTH, (int) (cycleDays - notifyBeforePeriod));
            }

            alarmManager.cancel(broadcast); //Cancelling any existing alarms
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nextAlarm.getTimeInMillis(), intervalMillis, broadcast);
            setCycleStatus(context, true);
        }

        //Add mens days to the calendar for 2 years
        for (Integer k = 0; k <= 24; k++) {

            for (Integer i = 0; i < mensDays; i++) {
                compactCalendarView.addEvent(new CalendarDayEvent(cal.getTimeInMillis(), Color.argb(255, 235, 147, 147)), false);
                cal.add(Calendar.DATE, 1);
            }

            //Add ovulating days to the calendar for 10 years
            daysBeforeOvulation = cycleDays - 17;
            cal.add(Calendar.DATE, -mensDays);
            cal.add(Calendar.DATE, daysBeforeOvulation);

            for (Integer j = 0; j < ovulationDays; j++) {
                cal.add(Calendar.DATE, 1);
                compactCalendarView.addEvent(new CalendarDayEvent(cal.getTimeInMillis(), Color.argb(140, 0, 138, 230)), false);
            }

            cal.add(Calendar.DATE, -(daysBeforeOvulation + ovulationDays)); //Reset calender day to the previous month first mens day
            cal.add(Calendar.DATE, cycleDays);
        }

        CalendarApp.gotoToday();
    }

    //Show settings
    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public static void gotoToday() {

        // Set any date to navigate to particular date
        compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
    }

    public static void setCycleStatus(Context context, boolean status) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(InitialSettingsActivity.IS_CYCLE_CREATED, status);
        editor.apply();
    }

}
