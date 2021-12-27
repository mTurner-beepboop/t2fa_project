package com.turnerm.testapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationIntentService extends JobIntentService {
    private static final int NOTIFICATION_ID = 3;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationIntentService.class, NOTIFICATION_ID, work);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleWork(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this, getString(R.string.CHANNEL_ID));
        builder.setContentTitle(getString(R.string.n_title)); //Add title
        builder.setContentText(getString(R.string.n_desc)); //Add body text
        builder.setSmallIcon(R.drawable.icon_foreground); //Add an icon if we have one
        builder.setPriority(Notification.PRIORITY_HIGH);

        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }
}
