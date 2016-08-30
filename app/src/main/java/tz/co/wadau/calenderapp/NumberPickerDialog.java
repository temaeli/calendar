package tz.co.wadau.calenderapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;

/**
 * Created by admin on 21-Aug-16.
 */
public class NumberPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {

//    private static final String YEAR = "year";
//    private static final String MONTH = "month";
//    private static final String DAY = "day";

    private final NumberPicker mNumberPicker;
    private final OnNumberSetListener mNumberSetListener;
//    private final Calendar mCalendar;

//    private boolean mTitleNeedsUpdate = true;

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnNumberSetListener {

        /**
         * @param view The view associated with this listener.
         * @param value The value that was set.
         */
        void onNumberSet(NumberPicker view, int value);
    }

//    /**
//     * @param context The context the dialog is to run in.
//     * @param callBack How the parent is notified that the date is set.
//     */
//    public NumberPickerDialog(Context context, OnNumberSetListener callBack) {
//        this(context, callBack);
//    }
//
//    static int resolveDialogTheme(Context context, int resid) {
//        if (resid == 0) {
//            final TypedValue outValue = new TypedValue();
//            context.getTheme().resolveAttribute(R.attr.NumberPickerDialogTheme, outValue, true);
//            return outValue.resourceId;
//        } else {
//            return resid;
//        }
//    }

//    /**
//     * @param context The context the dialog is to run in.
//     * @param theme the theme to apply to this dialog
//     * @param listener How the parent is notified that the date is set.
//     * @param year The initial year of the dialog.
//     * @param monthOfYear The initial month of the dialog.
//     * @param dayOfMonth The initial day of the dialog.
//     */
    public NumberPickerDialog(Context context, OnNumberSetListener listener) {
        super(context);
//
        mNumberSetListener = listener;
//        mCalendar = Calendar.getInstance();
//
        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.number_picker, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
        setTitle(R.string.select_number);
//        setButtonPanelLayoutHint(LAYOUT_HINT_SIDE);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
//        mNumberPicker.setMinValue(0);
//        mNumberPicker.setMaxValue(10);
//        mNumberPicker.init(year, monthOfYear, dayOfMonth, this);
//        mNumberPicker.setValidationCallback(mValidationCallback);
    }

//    @Override
//    public void onDateChanged(DatePicker view, int year, int month, int day) {
//        mNumberPicker.init(year, month, day, this);
//        updateTitle(year, month, day);
//    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mNumberSetListener != null) {
                    // Clearing focus forces the dialog to commit any pending
                    // changes, e.g. typed text in a NumberPicker.
                    mNumberPicker.clearFocus();
                    mNumberSetListener.onNumberSet(mNumberPicker, mNumberPicker.getValue());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    /**
     * Gets the {@link DatePicker} contained in this dialog.
     *
     * @return The calendar view.
     */
    public NumberPicker getNumberPicker() {
        return mNumberPicker;
    }

    /**
     * Sets the current date.
     *
     * @param year The date year.
     * @param monthOfYear The date month.
     * @param dayOfMonth The date day of month.
     */
//    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
//        mNumberPicker.updateDate(year, monthOfYear, dayOfMonth);
//    }

//    private void updateTitle(int year, int month, int day) {
//        if (!mNumberPicker.getCalendarViewShown()) {
//            mCalendar.set(Calendar.YEAR, year);
//            mCalendar.set(Calendar.MONTH, month);
//            mCalendar.set(Calendar.DAY_OF_MONTH, day);
//            String title = DateUtils.formatDateTime(mContext,
//                    mCalendar.getTimeInMillis(),
//                    DateUtils.FORMAT_SHOW_DATE
//                            | DateUtils.FORMAT_SHOW_WEEKDAY
//                            | DateUtils.FORMAT_SHOW_YEAR
//                            | DateUtils.FORMAT_ABBREV_MONTH
//                            | DateUtils.FORMAT_ABBREV_WEEKDAY);
//            setTitle(title);
//            mTitleNeedsUpdate = true;
//        } else {
//            if (mTitleNeedsUpdate) {
//                mTitleNeedsUpdate = false;
//                setTitle(R.string.date_picker_dialog_title);
//            }
//        }
//    }

//    @Override
//    public Bundle onSaveInstanceState() {
//        final Bundle state = super.onSaveInstanceState();
//        state.putInt(YEAR, mNumberPicker.getYear());
//        state.putInt(MONTH, mNumberPicker.getMonth());
//        state.putInt(DAY, mNumberPicker.getDayOfMonth());
//        return state;
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        final int year = savedInstanceState.getInt(YEAR);
//        final int month = savedInstanceState.getInt(MONTH);
//        final int day = savedInstanceState.getInt(DAY);
//        mNumberPicker.init(year, month, day, this);
//    }

//    private final ValidationCallback mValidationCallback = new ValidationCallback() {
//        @Override
//        public void onValidationChanged(boolean valid) {
//            final Button positive = getButton(BUTTON_POSITIVE);
//            if (positive != null) {
//                positive.setEnabled(valid);
//            }
//        }
//    };
}

