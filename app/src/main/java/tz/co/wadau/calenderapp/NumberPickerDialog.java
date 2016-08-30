package tz.co.wadau.calenderapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/**
 * Created by admin on 21-Aug-16.
 */
public class NumberPickerDialog extends AlertDialog implements DialogInterface.OnClickListener {

    private final NumberPicker mNumberPicker;
    private final OnNumberSetListener mNumberSetListener;

    public interface OnNumberSetListener {

        /**
         * @param view The view associated with this listener.
         * @param value The value that was set.
         */
        void onNumberSet(NumberPicker view, int value);
    }

    public NumberPickerDialog(Context context, OnNumberSetListener listener) {
        super(context);
//
        mNumberSetListener = listener;

        final Context themeContext = getContext();
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.number_picker, null);
        setView(view);
        setButton(BUTTON_POSITIVE, themeContext.getString(R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(R.string.cancel), this);
        setTitle(R.string.select_number);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
    }

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
     * Gets the {@link NumberPicker} contained in this dialog.
     *
     * @return The nuber view.
     */
    public NumberPicker getNumberPicker() {
        return mNumberPicker;
    }
}

