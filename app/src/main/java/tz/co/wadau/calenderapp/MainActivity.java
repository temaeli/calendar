package tz.co.wadau.calenderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String isFirstRun = sharedPreferences.getString(InitialSettingsActivity.IS_FIRST_RUN, "");

        if(isFirstRun.toString().equals("no")) {
            startActivity(new Intent(this, CalendarApp.class));
        }else {
            startActivity(new Intent(this, InitialSettingsActivity.class));
        }
    }
}
