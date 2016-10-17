package tz.co.wadau.calenderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import tz.co.wadau.calenderapp.customviews.DatePreference;

public class AlarmNotification {

    String TAG = AlarmNotification.class.getSimpleName();
    static final String NOTIFICATION_TITLE = "tz.co.wadau.calenderapp.NOTIFICATION_TITLE";
    static final String NOTIFICATION_CONTENT = "tz.co.wadau.calenderapp.NOTIFICATION_CONTENT";

    public void setAlarm(Context context){

        //Reading user's preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int cycleDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_CYCLE_DAYS, 28);
        String lastMonthMensDate = sharedPrefs.getString(SettingsFragment.KEY_PREF_LAST_MONTH_MENS_DATE, "2016-05-21");
        int lutealPhaseDays = sharedPrefs.getInt(SettingsFragment.KEY_PREF_LUTEAL_PHASE_DAYS, 14);
        int notifyBeforePeriod = sharedPrefs.getInt(SettingsFragment.KEY_PREF_PERIOD_NOTIFY_BEFORE_DAYS, 1); //Days to notifiy before period
        int notifyBeforeOvulation = sharedPrefs.getInt(SettingsFragment.KEY_PREF_OVULATION_NOTIFY_BEFORE_DAYS, 2); //Days to notifiy before ovulation
        boolean isPeriodNotificationEnabled = sharedPrefs.getBoolean(SettingsFragment.KEY_PREF_PERIOD_NOTIFICATIONS, true);
        boolean isOvulationNotificationEnabled = sharedPrefs.getBoolean(SettingsFragment.KEY_PREF_OVULATION_NOTIFICATIONS, true);

        int calendarYear = DatePreference.getYear(lastMonthMensDate);
        int calendarMonth = DatePreference.getMonth(lastMonthMensDate) - 1;
        int calendarDay = DatePreference.getDate(lastMonthMensDate);

        Calendar periodDate = Calendar.getInstance(Locale.getDefault());
        Calendar todayCalendar = Calendar.getInstance(Locale.getDefault());
        periodDate.set(calendarYear, calendarMonth, calendarDay, 0, 0, 1);

        int daysBeforeFertilityWindow = cycleDays - (lutealPhaseDays + 3);
        int daysBeforeOvulation = daysBeforeFertilityWindow + 2;

        long dateDiff = TimeUnit.MILLISECONDS.toDays(todayCalendar.getTimeInMillis() - periodDate.getTimeInMillis()); //days
        long alarmIn = (cycleDays - (dateDiff % cycleDays)) - notifyBeforePeriod; //days
        long ovulationNotificationIn = (daysBeforeOvulation - (dateDiff % cycleDays)) - notifyBeforeOvulation; //days
        long intervalMillis = TimeUnit.DAYS.toMillis((cycleDays));

        Calendar nextAlarm = Calendar.getInstance(Locale.getDefault());
        Calendar nextOvulation = Calendar.getInstance(Locale.getDefault());

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        Intent notificationIntent2 = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        notificationIntent.putExtra(NOTIFICATION_TITLE, "Period notification");
        notificationIntent.putExtra(NOTIFICATION_CONTENT,
                context.getString(R.string.period_notification) + " "
                        + notifyBeforePeriod + " "
                        + context.getString(R.string.day));
        PendingIntent broadcastPeriod = PendingIntent.getBroadcast(context, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationIntent2.putExtra(NOTIFICATION_TITLE, "Ovulation notification");
        notificationIntent2.putExtra(NOTIFICATION_CONTENT,
                context.getString(R.string.ovulation_notification) + " "
                        + notifyBeforeOvulation + " "
                        + context.getString(R.string.day));
        PendingIntent broadcastOvulation = PendingIntent.getBroadcast(context, 102, notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (isPeriodNotificationEnabled) {
            //Creating notification for the period day
            if (alarmIn < 0) {
                alarmIn = (alarmIn + cycleDays);
            }

            nextAlarm.add(Calendar.DAY_OF_MONTH, (int) alarmIn);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, nextAlarm.getTimeInMillis(), intervalMillis, broadcastPeriod);

            Log.d(TAG, "Creating period notification in next " +  alarmIn
                    + " days " + "repeating in "
                    + TimeUnit.MILLISECONDS.toDays(intervalMillis) + " days");
        }else {
            alarmManager.cancel(broadcastPeriod);
            Log.d(TAG, "Cancelling period notifications");
        }

        if (isOvulationNotificationEnabled) {
            //Create notification for ovulation day
            if (ovulationNotificationIn < 0) {
                ovulationNotificationIn = (ovulationNotificationIn + cycleDays);
            }

            nextOvulation.add(Calendar.DAY_OF_MONTH, (int) ovulationNotificationIn);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, nextOvulation.getTimeInMillis(), intervalMillis, broadcastOvulation);

            Log.d(TAG, "Creating ovulation notification in next " +  ovulationNotificationIn
                    + " days " + "repeating in "
                    + TimeUnit.MILLISECONDS.toDays(intervalMillis) + " days");
        }else {
            alarmManager.cancel(broadcastOvulation);
            Log.d(TAG, "Cancelling ovulation notifications");
        }
    }
}
