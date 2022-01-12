package com.turnerm.t2fa_app;

import android.graphics.Point;

/**
 * TODO - Add nice ref to team
 */
public class CustomPoint extends Point {

    public double size;

    public CustomPoint(int x, int y, double size) {
        super(x, y);
        this.size = size;
    }
}
