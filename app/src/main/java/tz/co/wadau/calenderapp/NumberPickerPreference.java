package tz.co.wadau.calenderapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;



public class NumberPickerPreference extends DialogPreference {

    private NumberPicker picker;
    private int value;
    private int minValue;
    private int maxValue;
    private boolean wrapSelectorWheel;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Reading custom custom attributes
        for (int i=0; i < attrs.getAttributeCount(); i++){
            String attr = attrs.getAttributeName(i);

            if(attr.equals("minValue")){
               minValue = Integer.valueOf(attrs.getAttributeValue(i));
            }
            if(attr.equals("maxValue")){
               maxValue = Integer.valueOf(attrs.getAttributeValue(i));
            }
            if(attr.equals("wrapSelectorWheel")){
                wrapSelectorWheel = Boolean.valueOf(attrs.getAttributeValue(i));
            }
        }
    }

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        picker = new NumberPicker(getContext());
        picker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(picker);

        return dialogView;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setWrapSelectorWheel(wrapSelectorWheel);
        picker.setValue(getValue());
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            picker.clearFocus();
            int newValue = picker.getValue();
            if (callChangeListener(newValue)) {
                setValue(newValue);
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, minValue);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(minValue) : (Integer) defaultValue);
    }

    public void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }

    public int getValue() {
        return this.value;
    }
}