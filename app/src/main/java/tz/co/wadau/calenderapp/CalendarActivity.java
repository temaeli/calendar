package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionMenu;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tz.co.wadau.calenderapp.customviews.MCUtils;
import tz.co.wadau.calenderapp.helper.MyCycleDbHelper;

import static tz.co.wadau.calenderapp.SettingsFragment.setLocale;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = CalendarActivity.class.getSimpleName();

    private Toolbar toolbar;
    private static ActionBar actionBar;
    public static CompactCalendarView compactCalendarView;
    int[] colorKeyImage = {R.drawable.ic_color_key_red_24dp, R.drawable.ic_color_key_blue_24dp};
    private static SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
    private FloatingActionMenu floatingActionMenu;
    Calendar todayCalendar = Calendar.getInstance(Locale.getDefault());
    String today = MCUtils.formatDate(todayCalendar.getTime());


    public CalendarActivity() throws ParseException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setLocale(getApplicationContext());
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calendar);

        String[] colorKeyDescription = {getApplicationContext().getString(R.string.period_days),
                getApplicationContext().getString(R.string.ovulation_days)};
        final Button periodStarted = (Button) findViewById(R.id.btn_period_started);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        // Setting default toolbar title to empty
        actionBar.setTitle(null);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        floatingActionMenu.setClosedOnTouchOutside(true);

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        //set initial title
        actionBar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                today = MCUtils.formatDate(dateClicked);
                Log.d(TAG, "Clicked date " + today);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                // Changes toolbar title on monthChange
                actionBar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));
            }
        });

        //Reading user settings then adding mentral and ovulation days to calendar
        compactCalendarView.addEvents(getEventsFromDb(this));
        gotoToday();

        //Adding key for menstrual cycle colors
        CustomListAdapter adapter = new CustomListAdapter(this, colorKeyImage, colorKeyDescription);
        ListView colorKeyList = (ListView) findViewById(R.id.color_key_list);
        colorKeyList.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_calendar);
        navigationView.setNavigationItemSelectedListener(this);

        periodStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periodStarted(today);
                compactCalendarView.removeAllEvents();
                compactCalendarView.addEvents(getEventsFromDb(getApplicationContext()));
                compactCalendarView.invalidate();
                gotoToday();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocale(getApplicationContext());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        //Pause for 300ms till navigation drawer is closed
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Handle navigation view item clicks here.
                Context context = getApplicationContext();
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_calendar:
                        startActivity(new Intent(context, CalendarActivity.class));
                        finish();
                        break;
                    case R.id.nav_cycle_history:
                        startActivity(new Intent(context, CycleHistoryActivity.class));
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(context, SettingsActivity.class));
                        break;
                    case R.id.nav_help:
                        startActivity(new Intent(context, HelpActivity.class));
                        break;
                }
            }
        }, 300);

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
            case R.id.action_today:
                gotoToday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Set any date to navigate to particular date
    public static void gotoToday() {
        compactCalendarView.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());
        actionBar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
    }

    public static void setCycleStatus(Context context, boolean status) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(InitialSettingsActivity.IS_CYCLE_CREATED, status);
        editor.apply();
    }

    public static List<Event> getEventsFromDb(Context context) {
        MyCycleDbHelper db = new MyCycleDbHelper(context);
        List<Event> eventList = null;
        try {
            eventList = db.getMensCycleDays(context);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        db.closeDb();

        return eventList;
    }

    public void showAddNote(View v) {
        floatingActionMenu.close(true);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), AddNoteActivity.class));
            }
        }, 200);
    }

    public void showEditPeriodDays(View view) {
        floatingActionMenu.close(true);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), EditPeriodDaysActivity.class));
            }
        }, 200);
    }

    public void periodStarted(String date) {
        MyCycleDbHelper db = new MyCycleDbHelper(this);
        db.deleteLastCycleFrom(date);
        db.deleteEventFrom(date);
        db.addMensCycleDaysFrom(this, date);
    }
}
