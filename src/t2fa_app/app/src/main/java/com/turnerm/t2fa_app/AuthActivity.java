package com.turnerm.t2fa_app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.turnerm.t2fa_app.Objects.AuthObject;
import com.turnerm.t2fa_app.Objects.CreditCard;
import com.turnerm.t2fa_app.Objects.Cube;
import com.turnerm.t2fa_app.Objects.Squares;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

public class AuthActivity extends Activity {

    public static int count = 0;
    private File file;
    private boolean isButtonPushed = false;
    private AuthObject object = null;
    private boolean isActive;
    private Timer timer = new Timer();

    /**
     * Main driver of the activity, this'll basically check if the object has been used right, advance phases, and write to file once authentication is over, success or fail
     * @param points
     */
    public void setPoints(final ArrayList<CustomPoint> points){
        //This is gonna be complicated a bit

        //For cube, this will be called 4 times, with the last being the footprint touch
        //For pendant, this should be called once
        //For credit card, this ill be called many times, for each of the touches of the dots on the 'safe lock' then the final being the footprint
        //For coin, I'm not 100% sure yet, as the move_event is still a bit weird to me, but if I'm right, this should be called multiple times, with the footprint initially, then the path points
        //As far as I'm aware, the other models won't be used
    }

    /**
     * Build the view and ensure anything that needs instantiating is instantiated
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_second);

        AuthView view = findViewById(R.id.authView); //id of the AuthView object on Fragment 2 should be called authView
        view.setActivity(this);

        //If any of the models require a button to 'submit' attempt, use this code here
        /**
         * Button button = findViewById(R.id. ...);
         *         button.setOnClickListener(new View.OnClickListener() {
         *             @Override
         *             public void onClick(View view) {
         *                 isButtonPushed = true;
         *             }
         *         });
         */

        //Since each participant will only be using one model, this can be set here
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String objectString = preferences.getString("model", "error");
        switch(objectString){ //TODO - Add all of the models that will be used here
            case "Cube":
                object = new Cube();
                break;
            case "Squares":
                object = new Squares();
                break;
            case "Credit Card":
                object = new CreditCard();
                break;
            default:
                break;
        }

        //Check if the auth object would require a button
        /**
         * if (object instanceOf ...){
         *      findViewById(R.id. ...).setVisibility(View.VISIBLE);
         * }
         * else {
         *      findViewById(R.id. ...).setVisibility(View.INVISIBLE);
         * }
         */

        file = new File(getApplicationContext().getFilesDir(), "log" + preferences.getString("id", "error") + ".csv");
    }

    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Stop back from doing anything as this would potentially break the read
     */
    @Override
    public void onBackPressed() {
    }
}
