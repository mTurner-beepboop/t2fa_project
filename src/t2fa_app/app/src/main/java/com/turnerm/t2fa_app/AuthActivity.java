package com.turnerm.t2fa_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

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

    public ArrayList<CustomPoint> points = new ArrayList<CustomPoint>();
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
            //Set the start time
            startTime = Calendar.getInstance().getTimeInMillis();

            //Change the text views to reflect the start of the authentication
            findViewById(R.id.startText).setVisibility(View.INVISIBLE);
            tv.setText("Attempts remaining: 3");
            tv.setVisibility(View.VISIBLE);

            //finally, set timerStarted to true, advance to phase attempt 1 and return
            timerStarted = true;
            this.phase = Phase.ATTEMPT_1;
            this.points = new ArrayList<CustomPoint>();
            return;
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
                //First set all the points
                object.addPoints(points);

                //Check the result of the added points
                result = object.getResult();
                checked = true;
                object.reset();
                break;
            case "Credit Card":
                object.addPoints(points);

                //Check for presence of footprint, if so, get result and set checked true
                CreditCard temp_obj = new CreditCard((CreditCard) this.object);
                if (temp_obj.getFootprint()){
                    System.out.println("Footprint found");
                    //Final touch has been made, evaluate result
                    result = object.getResult();
                    checked = true;
                    object.reset();
                    temp_obj.reset();
                }

                break;
            case "Cube":
                //Will take 4 separate touch down events each with different point number, this can be tracked within object instance
                object.addPoints(points);

                //Check for presence of final touch
                Cube temp_obj_cube = (Cube) this.object;
                if (temp_obj_cube.getFootprint()) { //Will actually keep accepting touches until the 2 side is touched to screen
                    //Final touch has been made, evaluate result
                    result = object.getResult();
                    checked = true;
                    object.reset();
                    temp_obj_cube.reset();
                }
                break;
            case "Pendant":
                //Set points in object
                object.addPoints(points);
                //Get result
                result = object.getResult();
                checked = true;
                object.reset();
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Where did this authentication take place? (home, work, etc): ");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                builder.setView(input);
                builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String where = input.getText().toString();
                        UtilityFuncs.saveToFile(file, true, attempts, object.toString(), endTime-startTime, where);

                    }
                });
                builder.setCancelable(false).show();

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

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Where did this authentication take place? (home, work, etc): ");

                        final EditText input = new EditText(this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        builder.setView(input);
                        builder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String where = input.getText().toString();
                                UtilityFuncs.saveToFile(file, false, attempts, object.toString(), endTime-startTime, where);

                            }
                        });
                        builder.setCancelable(false).show();


                        //Sort out the text fields and buttons
                        tv.setVisibility(View.INVISIBLE);
                        findViewById(R.id.homeButton).setVisibility(View.VISIBLE);
                        findViewById(R.id.endText).setVisibility(View.VISIBLE);

                        break;
                }
            }
            object.reset();
        }
        this.points = new ArrayList<CustomPoint>();
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
