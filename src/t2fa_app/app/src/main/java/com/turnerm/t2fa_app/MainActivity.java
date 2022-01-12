package com.turnerm.t2fa_app;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.turnerm.t2fa_app.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel(); //Allow notifications to be made by the app
        createNewNotification(); //Schedule a notification for an appropriate time

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        //Handle setting a new notification
        if (id == R.id.action_set_notify){
            /**
            Context context = getApplicationContext();
            CharSequence text = "A notification has been requested, this should appear in 60 seconds!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent notifyIntent = new Intent(this, NotificationReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (context, 3, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP,  System.currentTimeMillis() + (1000 * 10), pendingIntent); //Note, this method should just set an alarm for 60 seconds from the current time, non-repeating
             **/ //Old potential solution, include use of NotificationReceiver and NotificationIntentService
            //New Solution using BroadcastReceiver derived from GIST here https://gist.github.com/BrandonSmith/6679223
            Notification.Builder builder = new Notification.Builder(this, getString(R.string.CHANNEL_ID))
                    .setContentTitle(getString(R.string.n_title)) //Add title
                    .setContentText(getString(R.string.n_desc)) //Add body text
                    .setSmallIcon(R.drawable.notif_icon); //Add an icon if we have one;
            Notification notification = builder.build();

            Intent notificationIntent = new Intent(this, NotificationPublisher.class);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
            notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            long futureInMillis = SystemClock.elapsedRealtime() + 10000; //Currently 10 seconds
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * Code for setting up a notification reminder for the next authentication
     */
    private void createNewNotification(){
        Notification.Builder builder = new Notification.Builder(this, getString(R.string.CHANNEL_ID))
                .setContentTitle(getString(R.string.n_title)) //Add title
                .setContentText(getString(R.string.n_desc)) //Add body text
                .setSmallIcon(R.drawable.notif_icon); //Add an icon if we have one;
        Notification notification = builder.build();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + findAppropriateDelay(); //Will find tim in millis to trigger the next alarm
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    /**
     * Helper method to find an appropriate delay for creating a reminder notification to perform an authentication,
     * this is determined by taking a random length of time between 3 and 5 hours, then finding when the resulting
     * time would be, if this resulting time is in 'antisocial' hours, a new delay will be created to set the notification
     * for the next day
     *
     * @return delay to be used in creation of notification
     */
    private long findAppropriateDelay(){
        Calendar now = Calendar.getInstance();
        int curTime = now.get(Calendar.HOUR_OF_DAY);

        //Get a random number that fits in the specified time frame
        int max = 5; //5 hours
        int min = 3; //3 hours
        int randDelay = (int) Math.random() * (max - min + 1) + min;

        //Check if the new notification would fall into antisocial hours
        int cutoff = 21; // 9 p.m.
        if (curTime + randDelay > cutoff){
            //Find the number of hours until back in social hours (8 a.m.) and return time in millis
            int social = 8;
            int numHours = 0;
            if (curTime > social) { //For the cases of this, we are dealing with numbers from 21 to 23, and 0 to 7, so this is sufficient
                numHours = -(curTime - 24) + social;
            }else {
                numHours = social - curTime;
            }

            long delayMilli = 1000 * 60 * 60 * numHours;
            return delayMilli;
        }
        else{
            //Convert the chosen  number of hours into milliseconds and return
            long delayMilli = 1000 * 60 * 60 * randDelay;
            return delayMilli;
        }


    }

    /**
     * Code obtained from https://developer.android.com/training/notify-user/channels#java
     */
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.CHANNEL_ID), name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}