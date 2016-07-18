package tz.co.wadau.calenderapp;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;

public class SettingsActivity extends PreferenceActivity {
    public static final String KEY_PREF_SYNC_CONN = "pref_syncConnectionType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    // listener implementation
                    if (key.equals(KEY_PREF_SYNC_CONN)) {
                        Preference connectionPref = findPreference(key);
                        // Set summary to be the user-description for the selected value
                        connectionPref.setSummary(prefs.getString(key, ""));
                    }
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(listener);
    }


}
