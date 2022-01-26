package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import androidx.annotation.Nullable;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

/**
 * TODO - Make a nice reference to the research team
 * Class sourced from previous research tu_darmstadt etc
 */
public abstract class AuthObject {

    /**
     * Check the points list to find if the authentication was a success
     *
     * @return boolean of whether the authentication was success or not
     */
    public abstract boolean getResult();

    public abstract void addPoints(ArrayList<CustomPoint> points);

    /**
     * Legacy method from last project to draw a path that shows an outline of the object on the screen - might implement thins, might not
     *
     * @param x
     * @param y
     * @param px
     * @return Path for canvas object
     */
    public abstract Path getPath(float x, float y, float px);

    /**
     * Override of toString method to return the name of the object when required
     *
     * @return String name of object
     */
    public abstract String toString();

    public abstract void reset();
}