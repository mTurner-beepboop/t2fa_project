package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class CreditCard extends AuthObject {
    private ArrayList<CustomPoint> footprint = null;

    @Override
    public boolean getResult() {
        //Required checks - that the final event is the footprint touch, that each of the points touched are correct (turn the points into numbers from 0-9 then test against a preset passcode)

        return false;
    }

    public boolean getFootprint() {
        return (footprint != null);
    }

    @Override
    public void addPoints(ArrayList<CustomPoint> points){
        //Check if points sent correspond to footprint
        if (points.size() == 2){
            footprint = points;
            return;
        }
        else{
            //Do some check to find which point was touched and if it was correct
        }
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
