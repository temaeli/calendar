package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InitialSettingsActivity extends AppCompatActivity {

    static String IS_FIRST_RUN = "first_run";
    static String IS_CYCLE_CREATED = "cycle_created";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_settings);

        final TextInputLayout lastPeriodDateLayout = (TextInputLayout) findViewById(R.id.last_period_date_layout);
        final TextInputLayout periodDaysLayout = (TextInputLayout) findViewById(R.id.period_days_layout);
        final TextInputLayout cycleDaysLayout = (TextInputLayout) findViewById(R.id.cycle_days_layout);

        final EditText editLastMensDate = lastPeriodDateLayout.getEditText();
        final EditText editPeriodDays = periodDaysLayout.getEditText();
        final EditText editCycleDays = cycleDaysLayout.getEditText();

        final Button start = (Button) findViewById(R.id.btn_start);

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

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkEmpty(lastPeriodDateLayout);
                checkEmpty(periodDaysLayout);
                checkEmpty(cycleDaysLayout);

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

                    startActivity(new Intent(getApplicationContext(), CalendarApp.class));
                }

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

    public static boolean isFirstRun(Context context) {
        SharedPreferences sharedPreferences = Build.VERSION.SDK_INT >= 19 ?
                PreferenceManager.getDefaultSharedPreferences(context) :
                context.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(InitialSettingsActivity.IS_FIRST_RUN, true);
    }

}
