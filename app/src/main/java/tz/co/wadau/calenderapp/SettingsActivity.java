package tz.co.wadau.calenderapp;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
//    public static final String KEY_PREF_SYNC_CONN = "pref_syncConnectionType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .registerOnSharedPreferenceChangeListener(listener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .unregisterOnSharedPreferenceChangeListener(listener);
//    }


}
