package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.util.Locale;

import tz.co.wadau.calenderapp.customviews.DatePreference;
import tz.co.wadau.calenderapp.customviews.MCUtils;
import tz.co.wadau.calenderapp.customviews.NumberPickerPreference;
import tz.co.wadau.calenderapp.helper.MyCycleDbHelper;

public class SettingsFragment extends PreferenceFragment {
    public static final String KEY_PREF_MENS_DAYS = "prefs_mens_days";
    public static final String KEY_PREF_CYCLE_DAYS = "prefs_cycle_days";
    public static final String KEY_PREF_LAST_MONTH_MENS_DATE = "prefs_last_month_mens_date";
    public static final String KEY_PREF_LUTEAL_PHASE_DAYS = "prefs_luteal_phase_days";
    public static final String KEY_PREF_PERIOD_NOTIFICATIONS = "prefs_period_notifications";
    public static final String KEY_PREF_PERIOD_NOTIFY_BEFORE_DAYS = "prefs_period_notify_before_days";
    public static final String KEY_PREF_OVULATION_NOTIFICATIONS = "prefs_ovulation_notifications";
    public static final String KEY_PREF_OVULATION_NOTIFY_BEFORE_DAYS = "prefs_ovulation_notify_before_days";
    public static final String KEY_PREF_LANGUAGE = "prefs_language";
    public MyCycleDbHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_MENS_DAYS));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_CYCLE_DAYS));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_LAST_MONTH_MENS_DATE));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_LUTEAL_PHASE_DAYS));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_PERIOD_NOTIFY_BEFORE_DAYS));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_OVULATION_NOTIFY_BEFORE_DAYS));
        bindPreferenceSummaryToValue(findPreference(KEY_PREF_LANGUAGE));

        db = new MyCycleDbHelper(getActivity().getApplicationContext());
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    AlarmNotification alarmNotfn = new AlarmNotification();
                    Context context = getActivity().getApplicationContext();

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

                        case KEY_PREF_LUTEAL_PHASE_DAYS:
                            bindPreferenceSummaryToValue(findPreference(KEY_PREF_LUTEAL_PHASE_DAYS));
                            updateMentralCycleDays();
                            break;

                        case KEY_PREF_PERIOD_NOTIFICATIONS:
                            alarmNotfn.setAlarm(context);
                            break;

                        case KEY_PREF_PERIOD_NOTIFY_BEFORE_DAYS:
                            bindPreferenceSummaryToValue(findPreference(KEY_PREF_PERIOD_NOTIFY_BEFORE_DAYS));
                            updateMentralCycleDays();
                            break;

                        case KEY_PREF_OVULATION_NOTIFICATIONS:
                            alarmNotfn.setAlarm(context);
                            break;

                        case KEY_PREF_OVULATION_NOTIFY_BEFORE_DAYS:
                            bindPreferenceSummaryToValue(findPreference(KEY_PREF_OVULATION_NOTIFY_BEFORE_DAYS));
                            updateMentralCycleDays();
                            break;

                        case KEY_PREF_LANGUAGE:
                        bindPreferenceSummaryToValue(findPreference(KEY_PREF_LANGUAGE));
                            setLocale(context);
                            startActivity(new Intent(context, SettingsActivity.class));
                            getActivity().finish();
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
            preference.setSummary(MCUtils.formatToSystemDateFormat(getActivity().getApplicationContext(),
                    sharedPreferences.getString(preference.getKey(), "")));
        } else {
            preference.setSummary(sharedPreferences.getString(preference.getKey(), ""));
        }
    }



    public void updateMentralCycleDays() {
        CalendarActivity.compactCalendarView.removeAllEvents();
        CalendarActivity.setCycleStatus(getActivity().getApplicationContext(), false);
        db.addMensCycleDays(getActivity().getApplicationContext());
    }

    public static void setLocale(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String lang = sharedPreferences.getString(KEY_PREF_LANGUAGE, "en");
        Locale myLocale = new Locale(lang);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
