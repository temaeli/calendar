package tz.co.wadau.calenderapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.CalendarDayEvent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalenderApp extends AppCompatActivity {

    private Toolbar toolbar;
    CompactCalendarView compactCalendarView;
    Date lastMonthMensDate, ovulationStartDate;
    Integer mensDays, ovulationDays, cycleDays;

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_app);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        // Setting default toolbar title to empty
        actionBar.setTitle(null);

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.drawSmallIndicatorForEvents(true);
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

        mensDays = 3;
        cycleDays = 30;

        addMensCycleDays(mensDays, cycleDays);
        gotoToday();
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
    private void addMensCycleDays(int mensDays, int cycleDays) {

        ovulationDays = 7; // Avoiding days if you don't want to conceive and viceversa
        Double temp;
        Integer daysBeforeOvulation;

        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.set(2016, 4, 21, 0, 0, 1);

        for (Integer k = 0; k <= 11; k++) {

//        Add mens days to the calendar
            cal.add(Calendar.DATE, cycleDays);

            for (Integer i = 0; i < mensDays; i++) {

                compactCalendarView.addEvent(new CalendarDayEvent(cal.getTimeInMillis(), Color.argb(255, 255, 0, 0)), false);
                cal.add(Calendar.DATE, 1);
            }

            //Add ovulating days to the calendar
            temp = Math.floor(cycleDays / 2) - 4;
            daysBeforeOvulation = temp.intValue();
            cal.add(Calendar.DATE, -mensDays);
            cal.add(Calendar.DATE, daysBeforeOvulation);
            for (Integer j = 0; j < ovulationDays; j++) {
                cal.add(Calendar.DATE, 1);
                compactCalendarView.addEvent(new CalendarDayEvent(cal.getTimeInMillis(), Color.argb(255, 0, 255, 0)), false);
            }
            cal.add(Calendar.DATE, -(daysBeforeOvulation + ovulationDays)); //Reset calender day to the previous month first mens day
        }
    }

    //Show settings
    private void  showSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        Log.d("STARTED", "Activity started");
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
