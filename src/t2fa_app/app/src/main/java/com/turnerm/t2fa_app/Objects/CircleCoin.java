package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

public class CircleCoin extends AuthObject {
    @Override
    public boolean getResult(ArrayList<CustomPoint> points, float px) {
        return false;
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
