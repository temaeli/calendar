package tz.co.wadau.calenderapp.customviews;

import android.animation.ValueAnimator;
import android.widget.TextView;

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
}
