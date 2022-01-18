package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class CreditCard extends AuthObject {
    @Override
    public boolean getResult(ArrayList<CustomPoint> points, float px) {
        //Required checks - that the final event is the footprint touch, that each of the points touched are correct (turn the points into numbers from 0-9 then test against a preset passcode)

        return false;
    }

    @Override
    public Path getPath(float x, float y, float px) {
        return null;
    }

    @Override
    public String toString() {
        return "Credit Card";
    }
}
