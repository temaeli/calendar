package tz.co.wadau.calenderapp.helper;

import android.provider.BaseColumns;

public class MyCycleContract {

    //Event entries
    public static final class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "events";

        public static final String COLUMN_EVENT_DATE = "date";
        public static final String COLUMN_EVENT_COLOR = "color";
        public static final String COLUMN_EVENT_FIRST_PERIOD_DATE = "first_period_date";
    }

    public static final class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";

        public static final String COLUMN_CATEGORY_NAME = "name";
    }

    //    Event entries
    public static final class NoteEntry implements BaseColumns {
    public static final String TABLE_NAME = "notes";

        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_TEXT = "text";
    }
}
