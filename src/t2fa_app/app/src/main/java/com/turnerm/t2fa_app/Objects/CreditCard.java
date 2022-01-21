package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class CreditCard extends AuthObject {
    private ArrayList<CustomPoint> path = new ArrayList<>();
    private ArrayList<CustomPoint> footprint = null;
    private int[] combination = {0,4,6};

    @Override
    public boolean getResult() {
        //Required checks - that the final event is the footprint touch, that each of the points touched are correct (turn the points into numbers from 0-9 then test against a preset passcode)
        if (footprint == null){
            return false;
        }

        //PROBLEM - Current solution relies on points being the same, hence the object cannot slide at all, very precise
        ArrayList<Integer> nums = new ArrayList<>();
        int num = 0;
        CustomPoint[] tempPoint = {null, null};
        for (CustomPoint p : path){
            //Walk though the path list, translating points to numbers
            if (tempPoint[0] == null && tempPoint[1] == null){
                //This is the first point in the list
                nums.add(0);
                tempPoint[0] = p;
            }
            else{
                //If the point 2 steps ago appears in the same location, then indicated a switch in direction, hence add num to list and set to 1
                if (tempPoint[1].x == p.x && tempPoint[1].y == p.y){
                    nums.add(num);
                    num = 1;
                }
                //Otherwise, iterate num by one
                else{
                    num++;
                }
                tempPoint[1] = tempPoint[0];
                tempPoint[0] = p;
            }
        }
        nums.add(num);

        if (nums.toArray().equals(combination)){
            return true;
        }

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
            path.addAll(points); //Should only add one point at a time
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
