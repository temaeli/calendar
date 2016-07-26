package tz.co.wadau.calenderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalenderApp extends AppCompatActivity {

    private Toolbar toolbar;
    private ActionBar actionBar;
    CompactCalendarView compactCalendarView;
    String lastMonthMensDate;
    Integer mensDays, ovulationDays, cycleDays;

    int[] colorKeyImage = {R.drawable.ic_color_key_red_24dp, R.drawable.ic_color_key_blue_24dp};
    String[] colorKeyDescription = {"Period days", "Ovulation days"};

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

                Toast.makeText(CalenderApp.this, "Date : " + dateClicked.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // Changes toolbar title on monthChange
                actionBar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
            }

        });

        //Reading user settings then adding mentral and ovulation days to calendar
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mensDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_MENS_DAYS, 3);
        cycleDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_CYCLE_DAYS, 28);
        lastMonthMensDate = sharedPrefs.getString(SettingsFragment.KEY_PREF_LAST_MONTH_MENS_DATE, "2016-05-21");

        addMensCycleDays(lastMonthMensDate, mensDays, cycleDays);
        gotoToday();

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

    // Adding dummy events in calendar view for April, may, june 2016
//    private void addDummyEvents() {
//
//        addEvents(compactCalendarView, Calendar.APRIL);
//        addEvents(compactCalendarView, Calendar.MAY);
//        addEvents(compactCalendarView, Calendar.JUNE);
//
//        // Refresh calendar to update events
//        compactCalendarView.invalidate();
//    }


    // Adding events from 1 to 6 days

//    private void addEvents(CompactCalendarView compactCalendarView, int month) {
//        currentCalender.setTime(new Date());
//        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
//        Date firstDayOfMonth = currentCalender.getTime();
//        for (int i = 0; i < 6; i++) {
//            currentCalender.setTime(firstDayOfMonth);
//            if (month > -1) {
//                currentCalender.set(Calendar.MONTH, month);
//            }
//            currentCalender.add(Calendar.DATE, i);
//            setToMidnight(currentCalender);
//            compactCalendarView.addEvent(new CalendarDayEvent(currentCalender.getTimeInMillis(), Color.argb(255, 255, 255, 255)), false);
//        }
//    }

    //Adding ovulation and mens days
    private void addMensCycleDays(String lastMonthMensDate, int mensDays, int cycleDays) {

        ovulationDays = 7; // Avoiding days if you don't want to conceive and viceversa
        Double temp;
        Integer daysBeforeOvulation;
        int calendarYear = DatePreference.getYear(lastMonthMensDate);
        int calendarMonth = DatePreference.getMonth(lastMonthMensDate) - 1;
        int calendarDay = DatePreference.getDate(lastMonthMensDate);

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(calendarYear, calendarMonth, calendarDay, 0, 0, 1);

        for (Integer k = 0; k <= 120; k++) {

//        Add mens days to the calendar for 10 years
            cal.add(Calendar.DATE, cycleDays);

            for (Integer i = 0; i < mensDays; i++) {

                compactCalendarView.addEvent(new CalendarDayEvent(cal.getTimeInMillis(), Color.argb(255, 235, 147, 147)), false);
                cal.add(Calendar.DATE, 1);
            }

            //Add ovulating days to the calendar for 10 years
            temp = Math.floor(cycleDays / 2) - 4;
            daysBeforeOvulation = temp.intValue();
            cal.add(Calendar.DATE, -mensDays);
            cal.add(Calendar.DATE, daysBeforeOvulation);
            for (Integer j = 0; j < ovulationDays; j++) {
                cal.add(Calendar.DATE, 1);
                compactCalendarView.addEvent(new CalendarDayEvent(cal.getTimeInMillis(), Color.argb(140, 0, 138, 230)), false);
            }
            cal.add(Calendar.DATE, -(daysBeforeOvulation + ovulationDays)); //Reset calender day to the previous month first mens day
        }
    }

    //Show settings
    private void  showSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


//    private void setToMidnight(Calendar calendar) {
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//    }


    public void gotoToday() {

        // Set any date to navigate to particular date
        compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
    }
}
