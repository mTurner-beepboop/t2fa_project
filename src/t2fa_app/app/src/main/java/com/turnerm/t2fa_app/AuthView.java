package com.turnerm.t2fa_app;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.turnerm.t2fa_app.Objects.AuthObject;

import java.util.ArrayList;

public class AuthView extends View {
    private AuthActivity activity;

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

        if (activity == null){
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getActionMasked() == 5) { //This will be used for one finger down, or another finger down
            activity.points.add(new CustomPoint((int) event.getX(), (int) event.getY(), event.getSize()));
            System.out.println("Finger down");
            return true;
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            System.out.println("Fingers up");
            activity.setPoints(activity.points);
            return true;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setActivity(AuthActivity act){this.activity = act;}
}
