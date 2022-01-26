package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import androidx.annotation.Nullable;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Since the getSize method hasn't been working and the AuthView hasn't been picking up historical values for path checking, this model appears to be out
 */
public class CircleCoin extends AuthObject {
    private ArrayList<CustomPoint> footprint = null;
    private ArrayList<CustomPoint> path = new ArrayList<CustomPoint>();
    private int inaccuracy = 1000;

    @Override
    public boolean getResult() {
        //Required checks - that there are 2 identical sized footprint points; that the path taken by the object is roughly a square

        //Check if a valid footprint was found at all
        if (footprint == null){
            System.out.println("No footprint found");
            return false;
        }

        //Perform check on the path
        //Check if the path taken is a square given some allowance of inaccuracy
        int x1, x2, y1, y2;
        //Collect x1, y1 from footprint location
        x1 = footprint.get(0).x;
        y1 = footprint.get(0).y;

        //Collect x2, y2 from max of point on path
        CustomPoint maxPoint = Collections.max(path);
        System.out.println(path.size());
        System.out.println("Size of point: "+ Double.toString(maxPoint.getSize()));
        x2 = maxPoint.x;
        y2 = maxPoint.y;
        System.out.println("Max : " + Integer.toString(x2) + " Max y: " + Integer.toString(y2));

        //First check if the path begins and ends in the same place (with allowance of inaccuracy)
        CustomPoint last = path.get(path.size()-1);
        System.out.println("Last X: "+ Integer.toString(last.x)+ " Last y: "+ Integer.toString(last.y)+ " First x: "+ Integer.toString(x1)+ " First y: "+ Integer.toString(y1));
        if (last.x < x1 - inaccuracy || last.x > x1 + inaccuracy || last.y < y1 - inaccuracy || last.y > y1 + inaccuracy){
            return false;
        }

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

    @Override
    public void reset() {
        footprint = null;
        path = new ArrayList<CustomPoint>();
    }
}
