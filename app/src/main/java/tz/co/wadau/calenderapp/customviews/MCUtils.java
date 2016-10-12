package tz.co.wadau.calenderapp.customviews;

import android.animation.ValueAnimator;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

import static android.R.attr.format;

public class MCUtils {

    public static void animateTextView(int initialValue, int finalValue, int duration, final TextView textview) {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(duration);
//        valueAnimator.setInterpolator(new BounceInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                textview.setText(valueAnimator.getAnimatedValue().toString());

            }
        });
        valueAnimator.start();
    }

    public static String formatDate(Date date){
        SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return formatted.format(date);
    }
}
