package com.turnerm.t2fa_app;

import android.graphics.Point;

/**
 * Class sourced from previous research tu_darmstadt etc
 */
public class CustomPoint extends Point implements Comparable<CustomPoint> {

    public double size;

    public CustomPoint(int x, int y, double size) {
        super(x, y);
        this.size = size;
    }

    public double getSize(){
        return size;
    }


    @Override
    public int compareTo(CustomPoint o) {
        if (this.x > o.x && this.y > o.y){
            return 1;
        }
        if (this.x < o.x && this.y < o.y){
            return -1;
        }
        return 0;
    }
}
