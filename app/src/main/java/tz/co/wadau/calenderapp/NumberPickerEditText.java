package tz.co.wadau.calenderapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.NumberPicker;

public class NumberPickerEditText extends DialogFragment implements NumberPickerDialog.OnNumberSetListener {

    int minVal;
    int maxVal;
    int defautValue;
    EditText editText;

    public NumberPickerEditText(int minValue, int maxValue, int defautValue, EditText editText){
        minVal = minValue;
        maxVal = maxValue;
        this.editText = editText;
        this.defautValue = defautValue;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        NumberPickerDialog nPicker = new NumberPickerDialog(getActivity(), this);
        NumberPicker numberPicker = nPicker.getNumberPicker();
        numberPicker.setMinValue(minVal);
        numberPicker.setMaxValue(maxVal);
        numberPicker.setValue(defautValue);
        return nPicker;
    }

    @Override
    public void onNumberSet(NumberPicker view, int value) {
        editText.setText(String.valueOf(value));
    }
}
