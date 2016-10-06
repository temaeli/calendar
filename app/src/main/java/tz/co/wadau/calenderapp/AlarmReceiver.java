package tz.co.wadau.calenderapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.media.action.DISPLAY_NOTIFICATION")) {
            final String TAG = AlarmReceiver.class.getSimpleName();
            Log.d(TAG, "Broadicast received with action " + intent.getAction());

            String notificationTitle = intent.getStringExtra(AlarmNotification.NOTIFICATION_TITLE);
            String notificationContent = intent.getStringExtra(AlarmNotification.NOTIFICATION_CONTENT);

            Intent notificationIntent = new Intent(context, CalendarApp.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(CalendarApp.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            Notification notification = builder.setContentTitle(notificationTitle)
                    .setContentText(notificationContent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(notificationTitle)
                    .setColor(context.getResources().getColor(R.color.colorPrimary))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }
    }
}
