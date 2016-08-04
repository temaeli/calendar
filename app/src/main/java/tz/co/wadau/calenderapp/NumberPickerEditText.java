package tz.co.wadau.calenderapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class NumberPickerEditText extends DialogFragment {

    NumberPicker picker;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        picker = new NumberPicker(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.width = 20;


        picker.setLayoutParams(layoutParams);
        picker.setMaxValue(10);
        picker.setMinValue(1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("This is dialog title")
                .setView(picker)
                .setPositiveButton("OK", new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        return builder.create();
    }
}
