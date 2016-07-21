package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
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
        return (picker);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setValue(2);
        picker.setMaxValue(7);
        picker.setMinValue(1);
        picker.setWrapSelectorWheel(false);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult){
            persistInt(picker.getValue());
        }
    }
//
//    @Override
//    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
//        int def = (defaultValue instanceof Number) ? (Integer) defaultValue
//                : ( defaultValue != null ) ? Integer.parseInt(defaultValue.toString()) : 1;
//        if ( restorePersistedValue ) {
//            this.initialValue = getPersistedInt(def);
//        }
//        else this.initialValue = (Integer) defaultValue;
//    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 1);
    }
}
