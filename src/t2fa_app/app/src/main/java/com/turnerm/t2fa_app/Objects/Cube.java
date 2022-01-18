package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class Cube extends AuthObject {
    @Override
    public boolean getResult(ArrayList<CustomPoint> points, float px) {
        //Required checks - that the final is the footprint touch, that each set of points is the correct size (ie that the right side of the cube has been touched)

        return false;
    }

    @Override
    public Path getPath(float x, float y, float px) {
        return null;
    }

    @Override
    public String toString() {
        return "Cube";
    }
}
