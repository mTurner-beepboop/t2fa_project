package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class Cube extends AuthObject {
    private int footprintNum = 2; //This could be changed if needed in a constructor method
    private ArrayList<ArrayList<CustomPoint>> points = new ArrayList<>();
    private int[] combination = {4,1,4,2};

    @Override
    public boolean getResult() {
        //Required checks - that the final is the footprint touch, that each set of points is the correct size (ie that the right side of the cube has been touched)
        //Iterate through the list of points and the combination, checking that they match, if one doesn't, return false, if they all match, return true

        int i = 0;
        for (ArrayList<CustomPoint> l : points){
            if (i>combination.length){ //Stops an index out of bounds error
                return false;
            }

            if (l.size() != combination[i]){
                return false;
            }
            i++;
        }

        return true;
    }

    public boolean getFootprint(){
        for (ArrayList<CustomPoint> l: this.points){
            if (l.size() == this.footprintNum){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addPoints(ArrayList<CustomPoint> points){
        this.points.add(points);
        return;
    }

    @Override
    public Path getPath(float x, float y, float px) {
        return null;
    }

    @Override
    public String toString() {
        return "Cube";
    }

    @Override
    public void reset() {
        points = new ArrayList<ArrayList<CustomPoint>>();
    }
}
