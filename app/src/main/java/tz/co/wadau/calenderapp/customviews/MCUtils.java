package tz.co.wadau.calenderapp.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.widget.TextView;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static long dateDiffInDays(Long HighMills, Long LowMills){
        return  TimeUnit.MILLISECONDS.toDays(HighMills - LowMills);
    }

    public static String formatToSystemDateFormat(Context context, String str) {
        //Reading system date format
        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
        String pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();

        SimpleDateFormat systemDateFormat = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(DatePreference.getYear(str), DatePreference.getMonth(str) - 1, DatePreference.getDate(str));
        return systemDateFormat.format(calendar.getTime());
    }
}
