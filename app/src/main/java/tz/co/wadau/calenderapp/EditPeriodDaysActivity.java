package tz.co.wadau.calenderapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class EditPeriodDaysActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_period_days);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_period_days_toolbar);
        setSupportActionBar(toolbar);
    }
}
