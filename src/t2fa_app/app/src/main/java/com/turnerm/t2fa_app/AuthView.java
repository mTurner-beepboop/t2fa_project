package com.turnerm.t2fa_app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.turnerm.t2fa_app.Objects.AuthObject;

import java.util.ArrayList;

public class AuthView extends View {
    private AuthActivity activity;
    private AuthObject object;

    public AuthView(Context context) {
        super(context);
    }

    public AuthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public AuthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        //Checks - Pendant will work fine, cube may need to have multiple point sets, coin should be okay

        AuthActivity.count = event.getPointerCount();
        if (event.getAction() == MotionEvent.ACTION_UP){
            AuthActivity.count--;
        }

        //For move action events, this should pick up a point for each of the batches of moves, and should be sufficient to check against the validation points
        ArrayList<CustomPoint> points = new ArrayList<>();

        if (AuthActivity.count > 0){
            for (int i = 0; i < AuthActivity.count; i++){
                points.add(new CustomPoint((int) event.getX(i), (int) event.getY(i), event.getSize(i)));
            }
        }

        if (activity != null){
            activity.setPoints(points);
        }
        return true;
    }

    public void setObject(AuthObject obj){this.object = obj;}

    public void setActivity(AuthActivity act){this.activity = act;}
}
