package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class Squares extends AuthObject {

    @Override
    public boolean getResult() {
        return false;
    }


    @Override
    public void addPoints(ArrayList<CustomPoint> points){
        return;
    }

    @Override
    public Path getPath(float x, float y, float px) {
        return null;
    }

    @Override
    public String toString() {
        return "Squares";
    }

    @Override
    public void reset() {
        return;
    }
}
