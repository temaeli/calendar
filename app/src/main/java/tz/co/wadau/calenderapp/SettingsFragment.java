package tz.co.wadau.calenderapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SettingsFragment extends PreferenceFragment {
    public static final String KEY_PREF_MENS_DAYS = "prefs_mens_days";
    public static final String KEY_PREF_CYCLE_DAYS = "prefs_cycle_days";
    public static final String KEY_PREF_LAST_MONTH_MENS_DATE = "prefs_last_month_mens_date";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_MENS_DAYS));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_CYCLE_DAYS));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_LAST_MONTH_MENS_DATE));
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    // Set summary to be the user-description for the selected value
                    switch (key) {
                        case KEY_PREF_MENS_DAYS:
                            bindPreferenceSummaryToValue(findPreference(KEY_PREF_MENS_DAYS));
                            updateMentralCycleDays();
                            break;

                        case KEY_PREF_CYCLE_DAYS:
                            bindPreferenceSummaryToValue(findPreference(KEY_PREF_CYCLE_DAYS));
                            updateMentralCycleDays();
                            break;

                        case KEY_PREF_LAST_MONTH_MENS_DATE:
                            bindPreferenceSummaryToValue(findPreference(KEY_PREF_LAST_MONTH_MENS_DATE));
                            updateMentralCycleDays();
                            break;
                    }
                }
            };

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
        super.onPause();
    }

    private void bindPreferenceSummaryToValue(Preference preference) {
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        if (preference instanceof NumberPickerPreference) {
            preference.setSummary(String.valueOf(sharedPreferences.getInt(preference.getKey(), 0)));
        } else if (preference instanceof DatePreference) {
            preference.setSummary(formatToSystemDateFormat(sharedPreferences.getString(preference.getKey(), "")));
        } else {
            preference.setSummary(sharedPreferences.getString(preference.getKey(), ""));
        }
    }

    public String formatToSystemDateFormat(String str) {
        //Reading system date format
        Format dateFormat = android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext());
        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();

        SimpleDateFormat systemDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(DatePreference.getYear(str), DatePreference.getMonth(str) - 1, DatePreference.getDate(str));
        return systemDateFormat.format(calendar.getTime());
    }

    public void updateMentralCycleDays() {
        CalendarApp.compactCalendarView.removeAllEvents();
        CalendarApp.setCycleStatus(getActivity().getApplicationContext(), false);
        CalendarApp.addMensCycleDays(getActivity().getApplicationContext());
    }
}
