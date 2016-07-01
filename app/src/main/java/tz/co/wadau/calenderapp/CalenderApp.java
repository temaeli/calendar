package tz.co.wadau.calenderapp;

import android.graphics.Interpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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


    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_app);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
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

        Calendar cal = Calendar.getInstance();
        mensDays = 3;
        cycleDays = 30;
        ovulationDays = 7;
        Double temp;
        Integer daysBeforeOvulation;
        cal.set(2016,4,19,0,0,0);

        for(Integer k = 0; k <= 11; k++){

//        Add mens days to the calendar
            cal.add(Calendar.DATE, cycleDays);

            for (Integer i = 0; i < mensDays; i++){

                compactCalendarView.addEvent(new CalendarDayEvent(cal.getTimeInMillis(), Color.argb(255, 255, 0, 0)), false);
                cal.add(Calendar.DATE, 1);
            }

            //Add ovulating days to the calendar
            temp = (Double) Math.floor(cycleDays/2) - 4;
            daysBeforeOvulation = temp.intValue();
            cal.add(Calendar.DATE, -mensDays);
            cal.add(Calendar.DATE, daysBeforeOvulation);
            for (Integer j = 0; j < ovulationDays; j++){
                cal.add(Calendar.DATE, 1);
                compactCalendarView.addEvent(new CalendarDayEvent(cal.getTimeInMillis(), Color.argb(255, 0, 255, 0)), false);
            }
            cal.add(Calendar.DATE, -(daysBeforeOvulation + ovulationDays)); //Reset calender day to the previous month first mens day
        }

        gotoToday();


//        addDummyEvents();

        //  gotoToday();


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


    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }



    public void gotoToday() {

        // Set any date to navigate to particular date
        compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
    }
}
