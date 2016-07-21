package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;


public class NumberPickerPreference extends DialogPreference {

    NumberPicker picker;
    Integer initialValue;


    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateDialogView() {
        picker = new NumberPicker(getContext());

        // setCalendarViewShown(false) attribute is only available from API level 11
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            picker.setCalendarViewShown(false);
//        }

        return (picker);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setValue(3);
        picker.setMaxValue(7);
        picker.setMinValue(1);
        picker.setWrapSelectorWheel(false);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);
        if ( which == DialogInterface.BUTTON_POSITIVE ) {
//            this.initialValue = picker.getCurrent();
            persistInt(picker.getValue());
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        int def = ( defaultValue instanceof Number ) ? (Integer)defaultValue
                : ( defaultValue != null ) ? Integer.parseInt(defaultValue.toString()) : 1;
        if ( restorePersistedValue ) {
            this.initialValue = getPersistedInt(def);
        }
        else this.initialValue = (Integer) defaultValue;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 1);
    }
}
