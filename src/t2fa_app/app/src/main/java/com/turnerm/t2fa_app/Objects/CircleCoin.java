package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import androidx.annotation.Nullable;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class CircleCoin extends AuthObject {
    private ArrayList<CustomPoint> footprint = null;
    private ArrayList<CustomPoint> path = new ArrayList<CustomPoint>();

    @Override
    public boolean getResult() {
        //Required checks - that there are 2 identical sized footprint points; that the path taken by the object touches 3 specific points on the screen in the correct order

        //Check if a valid footprint was found at all
        if (footprint == null){
            return false;
        }

        //Perform check on the path
        //Put some points on the screen here
        //Put some booleans corresponding to point being touched here
        //Iterate through the list of points, check a boolean off if it hits one of the points, but if it hits the wrong one return false

        return true;
    }

    @Override
    public void addPoints(ArrayList<CustomPoint> points) {
        //If points size is 2 then this is the first touch, and the points contain the footprint of the object
        if (points.size() == 2){
            this.footprint = points;
            return;
        }
        //Otherwise, this is path information
        else {
            path.addAll(points);
            return;
        }
    }

    @Override
    public Path getPath(float x, float y, float px) {
        return null;
    }

    @Override
    public String toString() {
        return "Coin";
    }
}
