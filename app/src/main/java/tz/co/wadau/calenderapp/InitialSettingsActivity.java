package tz.co.wadau.calenderapp;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class InitialSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_settings);

        EditText lastMensDate = (EditText) findViewById(R.id.edit_last_month_period_start);
        lastMensDate.setInputType(InputType.TYPE_NULL);
    }

    public void showDatePickerDialog(View v) {
        DatePickerEditText newFragment = new DatePickerEditText();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showNumberPickerDialog(View v){
        NumberPickerEditText newNumberFragment = new NumberPickerEditText();
        newNumberFragment.show(getSupportFragmentManager(), "numberPicker");
    }
}
