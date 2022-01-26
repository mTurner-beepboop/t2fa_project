package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

public class Pendant extends AuthObject {
    private ArrayList<CustomPoint> points = new ArrayList<>();
    private int footprintNum = 3;

    @Override
    public boolean getResult() {
        //Required checks - that the number of points is the footprint num + 1, size of one of the touch points is greater than the other 3
        if (points.size() != footprintNum + 1){
            return false;
        }

        //Rough check on size for right now, make sure one point is larger than the rest
        CustomPoint axis = points.stream().max(Comparator.comparing(p -> p.getSize())).get();
        points.remove(axis);

        for (CustomPoint p: points){
            if (p.getSize() >= axis.getSize()){
                return false;
            }
        }

        return true;
    }


    @Override
    public void addPoints(ArrayList<CustomPoint> points){
        this.points.addAll(points);
    }

    @Override
    public Path getPath(float x, float y, float px) {
        return null;
    }

    @Override
    public String toString() {
        return "Pendant";
    }

    @Override
    public void reset(){
        points = new ArrayList<CustomPoint>();
    }
}
