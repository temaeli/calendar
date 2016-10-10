package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tz.co.wadau.calenderapp.customviews.DatePreference;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = CalendarActivity.class.getSimpleName();
    private Toolbar toolbar;
    private ActionBar actionBar;
    public static CompactCalendarView compactCalendarView;
    static Calendar cal;
    int[] colorKeyImage = {R.drawable.ic_color_key_red_24dp, R.drawable.ic_color_key_blue_24dp};
    String[] colorKeyDescription = {"Period days", "Ovulation days (Fertility window)"};
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    static int luteralPhaseDays = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_app);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
         // Setting default toolbar title to empty
        actionBar.setTitle(null);

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
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

        //Adding key for menstrual cycle colors
        CustomListAdapter adapter = new CustomListAdapter(this, colorKeyImage, colorKeyDescription);
        ListView colorKeyList = (ListView) findViewById(R.id.color_key_list);
        colorKeyList.setAdapter(adapter);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (id){
            case R.id.nav_calendar :
                startActivity(new Intent(this, CalendarActivity.class));
                finish();
                break;
            case R.id.nav_cycle_history:
                startActivity(new Intent(this, CycleHistoryActivity.class));
                finish();
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                break;
            case R.id.nav_help:
                startActivity(new Intent(this, HelpActivity.class));
                finish();
                break;
        }


        return true;
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
            case R.id.action_help:
                showHelp();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Adding ovulation and mens days
    public static void addMensCycleDays(Context context) {

        //Reading user settings then adding menstrual and ovulation days to calendar
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int mensDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_MENS_DAYS, 3);
        int cycleDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_CYCLE_DAYS, 28);
        String lastMonthMensDate = sharedPrefs.getString(SettingsFragment.KEY_PREF_LAST_MONTH_MENS_DATE, "2016-05-21");
        int ovulationDays = 5;
        boolean cycleCreated = sharedPrefs.getBoolean(InitialSettingsActivity.IS_CYCLE_CREATED, true);

        int daysBeforeFertilityWindow = cycleDays - (luteralPhaseDays + 3);

        int calendarYear = DatePreference.getYear(lastMonthMensDate);
        int calendarMonth = DatePreference.getMonth(lastMonthMensDate) - 1;
        int calendarDay = DatePreference.getDate(lastMonthMensDate);

        cal = Calendar.getInstance(Locale.getDefault());
        cal.set(calendarYear, calendarMonth, calendarDay, 0, 0, 1);

        if (!cycleCreated) {
            AlarmNotification alarmNotification = new AlarmNotification();
            alarmNotification.setAlarm(context);
            setCycleStatus(context, true);
        }

        //Add mens days to the calendar for 1 years
        for (Integer k = 0; k <= 24; k++) {

            for (Integer i = 0; i < mensDays; i++) {
                compactCalendarView.addEvent(new Event(Color.argb(255, 235, 147, 147), cal.getTimeInMillis()), false);
                cal.add(Calendar.DATE, 1);
            }

            //Add ovulating days to the calendar for 10 years
            cal.add(Calendar.DATE, -mensDays);
            cal.add(Calendar.DATE, daysBeforeFertilityWindow);

            for (Integer j = 0; j < ovulationDays; j++) {
                compactCalendarView.addEvent(new Event(Color.argb(140, 0, 138, 230), cal.getTimeInMillis()), false);
                cal.add(Calendar.DATE, 1);
            }

            cal.add(Calendar.DATE, -(daysBeforeFertilityWindow + ovulationDays)); //Reset calender day to the previous month first mens day
            cal.add(Calendar.DATE, cycleDays);
        }

        gotoToday();
    }

    //Show settings
    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //Show help
    private void  showHelp(){
        Intent intent = new Intent(this, CycleHistoryActivity.class);
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
