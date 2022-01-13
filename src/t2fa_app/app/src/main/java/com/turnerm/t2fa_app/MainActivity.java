package com.turnerm.t2fa_app;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.turnerm.t2fa_app.Notification.NotificationPublisher;
import com.turnerm.t2fa_app.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private static boolean firstStart = true;
    private static Calendar firstDate;

    private static final String[] models = {"Cube", "Squares", "Credit Card", "etc"};

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

        //Add to allow for information to be saved
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        //Perform first start information collection
        if (firstStart){
            //Check if the ID has already been input in sharedPreferences
            if (!preferences.contains("id")) {
                //Get the participant's ID number and save
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Participant ID number: ");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);
                builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userID = input.getText().toString();
                        editor.putString("id", userID);
                        editor.apply();
                    }
                });
                builder.setCancelable(false).show();
            }

            //Check if the model has already been input in sharedPreferences
            if (!preferences.contains("model")) {
                //Get the participant's model and save
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Participant Model: ").setSingleChoiceItems(this.models, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userModel = models[which];
                        editor.putString("model", userModel);
                        editor.apply();
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false).show();
            }

            //TODO - Check if a file for storage of data has already been created, if not, do so



            //Set the first date of the study
            this.firstDate = Calendar.getInstance();



            //Finally, set firstStart to false
            firstStart = false;
        }

        TextView tv = (TextView) findViewById(R.id.participantNum);
        tv.setText("Participant " + preferences.getString("id", "-1"));

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
            CharSequence text = "A notification should appear in the next 10 seconds!";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();

            Notification notification = UtilityFuncs.createNotification(this);

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
        Notification notification = UtilityFuncs.createNotification(this);

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar now = Calendar.getInstance();
        int curHour = now.get(Calendar.HOUR_OF_DAY);
        long futureInMillis = SystemClock.elapsedRealtime() + UtilityFuncs.findAppropriateDelay(curHour); //Will find tim in millis to trigger the next alarm
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
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