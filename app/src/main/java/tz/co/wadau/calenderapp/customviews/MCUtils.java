package tz.co.wadau.calenderapp.customviews;

import android.animation.ValueAnimator;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public static long getTimeInMills(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = sdf.parse(dateStr);
        return date.getTime();
    }

    public static long dateDiffInDays(Long mills2, Long mills1){
        Log.d("some", String.valueOf(mills2));
        return  TimeUnit.MILLISECONDS.toDays(mills2 - mills1);
    }
}
