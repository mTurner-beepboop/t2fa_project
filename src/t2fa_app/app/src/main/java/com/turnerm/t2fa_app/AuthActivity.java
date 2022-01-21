package com.turnerm.t2fa_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.turnerm.t2fa_app.Objects.AuthObject;
import com.turnerm.t2fa_app.Objects.CircleCoin;
import com.turnerm.t2fa_app.Objects.CreditCard;
import com.turnerm.t2fa_app.Objects.Cube;
import com.turnerm.t2fa_app.Objects.Pendant;
import com.turnerm.t2fa_app.Objects.Squares;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class AuthActivity extends Activity {

    enum Phase {
        TIMER_START,
        ATTEMPT_1,
        ATTEMPT_2,
        ATTEMPT_3,
        FAIL,
        SUCCESS
    }

    public static int count = 0;
    private File file;
    private boolean isButtonPushed = false;
    private AuthObject object = null;
    private boolean isActive;
    private long startTime;
    private long endTime = 0;
    private boolean timerStarted = false;
    private Phase phase = Phase.TIMER_START;
    private int attempts = 1;

    /**
     * Main driver of the activity, this'll basically check if the object has been used right, advance phases, and write to file once authentication is over, success or fail
     * @param points
     */
    public void setPoints(final ArrayList<CustomPoint> points){
        //This is gonna be complicated a bit

        //TODO - TEST ALL OF THIS

        //For cube, this will be called 4 times, with the last being the footprint touch
        //For pendant, this should be called once
        //For credit card, this ill be called many times, for each of the touches of the dots on the 'safe lock' then the final being the footprint
        //For coin, I'm not 100% sure yet, as the move_event is still a bit weird to me, but if I'm right, this should be called multiple times, with the footprint initially, then the path points
        //As far as I'm aware, the other models won't be used

        //The authentication has already been completed, so no need to do anything else
        if (this.phase == Phase.SUCCESS || this.phase == Phase.FAIL){
            return;
        }

        //The View isn't active so don't bother registering anything
        if (!isActive){
            return;
        }

        TextView tv = (TextView) findViewById(R.id.attemptsRemaining);

        //First check that the timer has started
        if (this.phase == Phase.TIMER_START){
            if (points.size() != 0){
                return;
            }
            else {
                //Set the start time
                startTime = Calendar.getInstance().getTimeInMillis();

                //Change the text views to reflect the start of the authentication
                findViewById(R.id.startText).setVisibility(View.INVISIBLE);
                tv.setText("Attempts remaining: 3");
                tv.setVisibility(View.VISIBLE);

                //finally, set timerStarted to true, advance to phase attempt 1 and return
                timerStarted = true;
                this.phase = Phase.ATTEMPT_1;
                return;
            }
        }

        //Next check that we're not in one of the end states
        if (this.phase == Phase.FAIL || this.phase == Phase.SUCCESS){
            if (endTime == 0){
                endTime = Calendar.getInstance().getTimeInMillis();
            }
            isActive = false;
            findViewById(R.id.homeButton).setVisibility(View.VISIBLE);
        }

        boolean result = false;
        boolean checked = false; //Whether or not getResult has been called

        switch(object.toString()){
            case "Coin":
                //This is the case that the object has been lifted from the screen, for the coin shape, this will only occur at the end, so this is where we evaluate the result
                if (points.size() == 0){
                    result = object.getResult();
                    checked = true;
                }
                //This is the case that the motion event returned something, so here is information that the instance needs
                else {
                    object.addPoints(points);
                }
                break;
            case "Credit Card":
                //Touch event over, this will only matter if the footprint has already been detected - ie, the touch event up indicated the final touch has been made
                if (points.size() == 0){
                    CreditCard temp_obj = (CreditCard) this.object;
                    if (temp_obj.getFootprint()){
                        //Final touch has been made, evaluate result
                        result = object.getResult();
                        checked = true;
                    }
                }
                else{
                    object.addPoints(points);
                }
                break;
            case "Cube":
                //Will take 4 separate touch down events each with different point number, this can be tracked within object instance
                if (points.size() == 0) {
                    Cube temp_obj = (Cube) this.object;
                    if (temp_obj.getFootprint()) { //Will actually keep accepting touches until the 2 side is touched to screen
                        //Final touch has been made, evaluate result
                        result = object.getResult();
                        checked = true;
                    }
                }
                else{
                    object.addPoints(points);
                }
                break;
            case "Pendant":
                //This should be dealt with in one touch event, but break it up into action down and action up parts
                if (points.size() == 0){
                    //Object/finger has been lifted, get the result now
                    result = object.getResult();
                    checked = true;
                }
                else{
                    object.addPoints(points);
                }

                break;
            default:
                break;
        }

        if (checked){
            if (result) {
                //The validation has been a success
                phase = Phase.SUCCESS;
                isActive = false;
                endTime = Calendar.getInstance().getTimeInMillis();
                UtilityFuncs.saveToFile(file, true, attempts, object.toString(), endTime-startTime);

                //Sort out the text fields and buttons
                tv.setVisibility(View.INVISIBLE);
                findViewById(R.id.homeButton).setVisibility(View.VISIBLE);
                findViewById(R.id.endText).setVisibility(View.VISIBLE);
            }
            else{
                switch (phase){
                    case ATTEMPT_1:
                        phase = Phase.ATTEMPT_2;
                        attempts += 1;
                        tv.setText("Attempts remaining: 2");
                        break;
                    case ATTEMPT_2:
                        phase = Phase.ATTEMPT_3;
                        attempts += 1;
                        tv.setText("Attempts remaining: 1");
                        break;
                    case ATTEMPT_3:
                        phase = Phase.FAIL;
                        //Deal with what happens on a fail here
                        isActive = false;
                        endTime = Calendar.getInstance().getTimeInMillis();
                        UtilityFuncs.saveToFile(file, false, attempts, object.toString(), endTime-startTime);

                        //Sort out the text fields and buttons
                        tv.setVisibility(View.INVISIBLE);
                        findViewById(R.id.homeButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.endText).setVisibility(View.VISIBLE);

                        break;
                }
            }
        }
    }

    /**
     * Build the view and ensure anything that needs instantiating is instantiated
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.fragment_second);

        AuthView view = findViewById(R.id.authView); //id of the AuthView object on Fragment 2 should be called authView
        view.setActivity(this);

        isActive = true;

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
        System.out.println(objectString);
        switch(objectString){
            case "Cube":
                object = new Cube();
                break;
            case "Squares":
                object = new Squares();
                break;
            case "Credit Card":
                object = new CreditCard();
                break;
            case "Pendant":
                object = new Pendant();
                break;
            case "Coin":
                object = new CircleCoin();
                break;
            default:
                break;
        }

        //Deal with the text and button views
        findViewById(R.id.startText).setVisibility(View.VISIBLE);
        findViewById(R.id.attemptsRemaining).setVisibility(View.INVISIBLE);
        findViewById(R.id.homeButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.endText).setVisibility(View.INVISIBLE);

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

    /**
     * Stop back from doing anything as this would potentially break the read
     */
    @Override
    public void onBackPressed() {
    }

    public void onClickHome(View view){
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
