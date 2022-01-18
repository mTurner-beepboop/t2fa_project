package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import androidx.annotation.Nullable;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class CircleCoin extends AuthObject {
    private ArrayList<CustomPoint> footprint = null;
    private ArrayList<CustomPoint> path = new ArrayList<CustomPoint>();

    @Override
    public boolean getResult(ArrayList<CustomPoint> points, boolean last) {
        //Required checks - that there are 2 identical sized footprint points; that the path taken by the object touches 3 specific points on the screen in the correct order

        //This should never be called
        if (footprint == null){
            return false;
        }

        //Check that the footprint is valid
        if (footprint.size() != 2){
            return false;
        }

        //Check if last read
        if (!last){
            //Add points to the path
            path.addAll(points);
            return false;
        }
        else{

        }

        //Perform check on the path
        //Put some points on the screen here
        //Put some booleans corresponding to point being touched here
        //Iterate through the list of points, check a boolean off if it hits one of the points, but if it hits the wrong one return false

        return true;
    }

    /**
     * This variable will be used in the first part of getResult to check that the footprint is correct - for this study, it can actually be hardcoded, but doing this makes it slightly better practice if the app was to be expanded for more footprints
     * @param footprint
     */
    public void setFootprint(ArrayList<CustomPoint> footprint){
        this.footprint = footprint;
    }

    public boolean checkFootprintPresence(){
        return footprint != null;
    }

    @Override
    public Path getPath(float x, float y, float px) {
        return null;
    }

    @Override
    public String toString() {
        return "Circle Coin";
    }
}
