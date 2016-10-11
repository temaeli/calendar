package tz.co.wadau.calenderapp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tz.co.wadau.calenderapp.helper.MyCycleContract.CategoryEntry;
import tz.co.wadau.calenderapp.helper.MyCycleContract.EventEntry;

public class MyCycleDbHelper extends SQLiteOpenHelper {

    private static final String TAG = MyCycleDbHelper.class.getSimpleName();

    //Database name
    private static final String DATABASE_NAME = "mycycle.db";

    //Database version
    private static final int DATABASE_VERSION = 1;

    final String SQL_CREATE_EVENTS_TABLE = "CREATE TABLE " + EventEntry.TABLE_NAME + " ( "
            + EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + EventEntry.COLUMN_EVENT_DATE + " VARCHAR(255) NOT NULL "
            + EventEntry.COLUMN_EVENT_COLOR + " VARCAR(255) NOT NULL "
            + ");";

    final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " ( "
            + CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CategoryEntry.COLUMN_CATEGORY_NAME + " VARCHAR(255) NOT NULL "
            + ");";

    public MyCycleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_EVENTS_TABLE);
        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);

        // create new tables
        onCreate(db);
    }

}
