package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!isFirstRun(getApplicationContext())) {
            startActivity(new Intent(this, CalendarActivity.class));
            finish();
        }else {
            startActivity(new Intent(this, InitialSettingsActivity.class));
            finish();
        }
    }

    public static boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = Build.VERSION.SDK_INT >= 19 ?
                PreferenceManager.getDefaultSharedPreferences(context) :
                context.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(InitialSettingsActivity.IS_FIRST_RUN, true);
    }
}
