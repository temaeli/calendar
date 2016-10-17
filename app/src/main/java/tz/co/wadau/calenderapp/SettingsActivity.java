package tz.co.wadau.calenderapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import static tz.co.wadau.calenderapp.CalendarActivity.compactCalendarView;
import static tz.co.wadau.calenderapp.CalendarActivity.gotoToday;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        compactCalendarView.addEvents(CalendarActivity.getEventsFromDb(getApplicationContext()));
        gotoToday();

        super.onBackPressed();
    }


}
