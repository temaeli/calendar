package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import tz.co.wadau.calenderapp.customviews.MCUtils;
import tz.co.wadau.calenderapp.helper.MyCycleDbHelper;
import tz.co.wadau.calenderapp.models.MCNote;

public class AddNoteActivity extends AppCompatActivity {
    private final String TAG = AddNoteActivity.class.getSimpleName();
    MyCycleDbHelper db = new MyCycleDbHelper(this);
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.add_note_toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.add_note_date);
        editText = (EditText) findViewById(R.id.note_text);
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
                saveNote();
                return true;
            case android.R.id.home:
                checkUnsaved(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveNote() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        String text = editText.getText().toString();

        if (!TextUtils.isEmpty(text)) {
            MCNote note = new MCNote(MCUtils.formatDate(calendar.getTime()), editText.getText().toString());
            long noteId = db.insertNote(note);
            Log.d(TAG, "Note inserted with id " + noteId);
            finish();
        } else {
            editText.setError(getString(R.string.this_cannot_be_empty));
        }
    }

    public void checkUnsaved(final Context context) {
        String text = editText.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Discard changes?");

            builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Do nothing
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        checkUnsaved(this);
    }
}
