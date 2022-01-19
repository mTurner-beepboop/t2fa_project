package com.turnerm.t2fa_app;

import android.app.Notification;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class UtilityFuncs {

    /**
     * Helper method to find an appropriate delay for creating a reminder notification to perform an authentication,
     * this is determined by taking a random length of time between 3 and 5 hours, then finding when the resulting
     * time would be, if this resulting time is in 'antisocial' hours, a new delay will be created to set the notification
     * for the next day
     *
     * @return delay to be used in creation of notification
     */
    public static long findAppropriateDelay(int hour){
        //Get a random number that fits in the specified time frame
        int max = 5; //5 hours
        int min = 3; //3 hours
        int randDelay = (int) (Math.random() * (max - min + 1) + min);

        //Check if the new notification would fall into antisocial hours
        int cutoff = 21; // 9 p.m.
        int social = 8;
        if ((hour + randDelay > cutoff) || (hour + randDelay < social)){
            //Find the number of hours until back in social hours (8 a.m.) and return time in millis

            int numHours = 0;
            if (hour > social) { //For the cases of this, we are dealing with numbers from 18 to 23, and 0 to 7, so this is sufficient
                numHours = -(hour - 24) + social;
            }
            else {
                numHours = social - hour;
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

    public static Notification createNotification(Context context){
        Notification.Builder builder = new Notification.Builder(context, context.getString(R.string.CHANNEL_ID))
                .setContentTitle(context.getString(R.string.n_title)) //Add title
                .setContentText(context.getString(R.string.n_desc)) //Add body text
                .setSmallIcon(R.drawable.notif_icon); //Add an icon if we have one;
        Notification notification = builder.build();
        return notification;
    }

    public static boolean saveToFile(File file, boolean success, int attempts, String model, long timeTaken){
        //First construct the string to be put into the file
        Calendar today = Calendar.getInstance();
        String toFile = Long.toString(today.getTimeInMillis()) + "," + Boolean.toString(success) + "," + Integer.toString(attempts) + "," + Long.toString(timeTaken) + "," + model;

        //Check if file exists
        if (!file.exists()){
            return false;
        }

        //Then try to open the file and save it
        try {
            FileOutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(toFile.getBytes());
            outputStream.write(System.getProperty("line.separator").getBytes());
            outputStream.close();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
