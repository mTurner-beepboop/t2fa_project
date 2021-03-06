package com.turnerm.t2fa_app.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.turnerm.t2fa_app.Notification.NotificationIntentService;

/**
 * CLASS NO LONGER IN USE
 */
public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, NotificationIntentService.class);
        NotificationIntentService.enqueueWork(context, intent1);

        CharSequence text = "The notification has been queued!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        //context.startService(intent1);
    }
}
