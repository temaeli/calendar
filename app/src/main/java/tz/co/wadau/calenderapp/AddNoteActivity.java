package tz.co.wadau.calenderapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import tz.co.wadau.calenderapp.customviews.MCUtils;

public class AddNoteActivity extends AppCompatActivity {
    private final String TAG = AddNoteActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_note_toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.add_note_date);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        textView.setText(MCUtils.formatToSystemDateFormat(this, MCUtils.formatDate(calendar.getTime())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                Log.d(TAG, "save note");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
