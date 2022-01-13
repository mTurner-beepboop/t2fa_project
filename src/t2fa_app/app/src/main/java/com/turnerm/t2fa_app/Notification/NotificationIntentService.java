package com.turnerm.t2fa_app.Notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.turnerm.t2fa_app.MainActivity;
import com.turnerm.t2fa_app.R;

/**
 * CLASS NO LONGER IN USE
 */
public class NotificationIntentService extends JobIntentService {
    private static final int NOTIFICATION_ID = 3;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, NotificationIntentService.class, NOTIFICATION_ID, work);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleWork(Intent intent) {
        System.out.println("Work being done");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setContentTitle(getString(R.string.n_title)) //Add title
            .setContentText(getString(R.string.n_desc)) //Add body text
            .setSmallIcon(R.drawable.notif_icon) //Add an icon if we have one
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, builder.build());
    }
}
