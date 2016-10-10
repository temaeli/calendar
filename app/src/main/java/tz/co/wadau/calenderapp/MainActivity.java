package tz.co.wadau.calenderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Build.VERSION.SDK_INT >= 23 ? setTheme(R.style.SplashTheme);
//        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        if(!InitialSettingsActivity.isFirstRun(getApplicationContext())) {

            startActivity(new Intent(this, CalendarActivity.class));
        }else {
            startActivity(new Intent(this, InitialSettingsActivity.class));
        }
        finish();
    }


}
