package tz.co.wadau.calenderapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String>  {
    private final Activity context;
    private final String[] colorKeyDescription;
    private final int[] colorKeyImage;

    public CustomListAdapter(Activity context, int[] colorKeyImage, String[] colorKeyDescription) {
        super(context, R.layout.color_keys_list, colorKeyDescription);
        this.context = context;
        this.colorKeyDescription = colorKeyDescription;
        this.colorKeyImage = colorKeyImage;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.color_keys_list, null, true);

        //Getting costom defined imageview and textview
        ImageView imageView = (ImageView) rowView.findViewById(R.id.color_key);
        TextView textView = (TextView) rowView.findViewById(R.id.color_description);

        imageView.setImageResource(colorKeyImage[position]);
        textView.setText(colorKeyDescription[position]);

        return rowView;
    }
}
