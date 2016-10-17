package tz.co.wadau.calenderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import tz.co.wadau.calenderapp.customviews.DatePickerEditText;
import tz.co.wadau.calenderapp.customviews.NumberPickerEditText;
import tz.co.wadau.calenderapp.helper.MyCycleDbHelper;

public class InitialSettingsActivity extends AppCompatActivity {

    static String IS_FIRST_RUN = "first_run";
    public static String IS_CYCLE_CREATED = "cycle_created";
    public TextInputLayout lastPeriodDateLayout;
    public TextInputLayout periodDaysLayout;
    public TextInputLayout cycleDaysLayout;
    public EditText editLastMensDate;
    public EditText editPeriodDays;
    public EditText editCycleDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_settings);

        lastPeriodDateLayout = (TextInputLayout) findViewById(R.id.last_period_date_layout);
        periodDaysLayout = (TextInputLayout) findViewById(R.id.period_days_layout);
        cycleDaysLayout = (TextInputLayout) findViewById(R.id.cycle_days_layout);

        editLastMensDate = lastPeriodDateLayout.getEditText();
        editPeriodDays = periodDaysLayout.getEditText();
        editCycleDays = cycleDaysLayout.getEditText();

        //Disable soft keyboard so the user can pick date and numbers from the dialog
        editLastMensDate.setInputType(InputType.TYPE_NULL);
        editPeriodDays.setInputType(InputType.TYPE_NULL);
        editCycleDays.setInputType(InputType.TYPE_NULL);

        editPeriodDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int periodDaysDefault = !TextUtils.isEmpty(editPeriodDays.getText().toString()) ?
                        Integer.valueOf(editPeriodDays.getText().toString()) : 3;
                NumberPickerEditText newNumberFragment =
                        NumberPickerEditText.newInstance(2, 7, periodDaysDefault, editPeriodDays.getId());
                newNumberFragment.show(getSupportFragmentManager(), "inputPeriodDays");
            }
        });

        editCycleDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cycleDaysDefault = !TextUtils.isEmpty(editCycleDays.getText().toString()) ?
                        Integer.valueOf(editCycleDays.getText().toString()) : 28;
                NumberPickerEditText newNumberFragment =
                        NumberPickerEditText.newInstance(20, 45, cycleDaysDefault, editCycleDays.getId());
                newNumberFragment.show(getSupportFragmentManager(), "inputCycleDays");
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DatePickerEditText newFragment = new DatePickerEditText();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void checkEmpty(TextInputLayout textInputLayout) {
        if (TextUtils.isEmpty(textInputLayout.getEditText().getText().toString())) {
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError(getString(R.string.this_cannot_be_empty));
            return;
        }
    }

    public void getStarted(View v) {
        checkEmpty(lastPeriodDateLayout);
        checkEmpty(periodDaysLayout);
        checkEmpty(cycleDaysLayout);

        final MyCycleDbHelper db = new MyCycleDbHelper(getApplicationContext());
        final String lastMensDate = editLastMensDate.getText().toString();
        final String periodDays = editPeriodDays.getText().toString();
        final String cycleDays = editCycleDays.getText().toString();

        if (!TextUtils.isEmpty(lastMensDate) &&
                !TextUtils.isEmpty(periodDays) &&
                !TextUtils.isEmpty(cycleDays)) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString(SettingsFragment.KEY_PREF_LAST_MONTH_MENS_DATE, lastMensDate);
            editor.putInt(SettingsFragment.KEY_PREF_MENS_DAYS, Integer.valueOf(periodDays));
            editor.putInt(SettingsFragment.KEY_PREF_CYCLE_DAYS, Integer.valueOf(cycleDays));
            editor.putBoolean(IS_FIRST_RUN, false);
            editor.putBoolean(IS_CYCLE_CREATED, false);
            editor.apply();

            db.addMensCycleDays(getApplicationContext());
            startActivity(new Intent(getApplicationContext(), CalendarActivity.class));
            finish();
        }
    }
}
