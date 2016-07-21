package tz.co.wadau.calenderapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

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
//        bindPreferenceSummaryToValue(findPreference(KEY_PREF_CYCLE_DAYS));
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
                            break;

                        case KEY_PREF_CYCLE_DAYS:
//                            bindPreferenceSummaryToValue(findPreference(KEY_PREF_CYCLE_DAYS));
                            break;

                        case KEY_PREF_LAST_MONTH_MENS_DATE:
                            bindPreferenceSummaryToValue(findPreference(KEY_PREF_LAST_MONTH_MENS_DATE));
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

        if(preference instanceof NumberPickerPreference){
            preference.setSummary(String.valueOf(sharedPreferences.getInt(preference.getKey(), 0)));
        }else {
            preference.setSummary(sharedPreferences.getString(preference.getKey(), ""));
        }
    }


}
