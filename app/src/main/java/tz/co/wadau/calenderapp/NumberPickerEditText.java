package tz.co.wadau.calenderapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;
import android.widget.NumberPicker;

public class NumberPickerEditText extends DialogFragment implements NumberPickerDialog.OnNumberSetListener {

    int editTextId;

    public NumberPickerEditText() {
    }

    public static NumberPickerEditText newInstance(int minValue, int maxValue, int defautValue, int editTextId) {
        NumberPickerEditText f = new NumberPickerEditText();
        Bundle args = new Bundle();
        args.putInt("minVal", minValue);
        args.putInt("maxValue", maxValue);
        args.putInt("defautValue", defautValue);
        args.putInt("editTextId", editTextId);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int minVal = getArguments().getInt("minVal");
        int maxVal = getArguments().getInt("maxValue");
        int defautValue = getArguments().getInt("defautValue");
         editTextId = getArguments().getInt("editTextId");

        NumberPickerDialog nPicker = new NumberPickerDialog(getActivity(), this);
        NumberPicker numberPicker = nPicker.getNumberPicker();
        numberPicker.setMinValue(minVal);
        numberPicker.setMaxValue(maxVal);
        numberPicker.setValue(defautValue);
        return nPicker;
    }

    @Override
    public void onNumberSet(NumberPicker view, int value) {
        EditText editText = (EditText) getActivity().findViewById(editTextId);
        editText.setText(String.valueOf(value));
    }
}
