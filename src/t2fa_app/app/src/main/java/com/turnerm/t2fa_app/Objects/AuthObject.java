package com.turnerm.t2fa_app.Objects;

import android.graphics.Path;

import com.turnerm.t2fa_app.CustomPoint;

import java.util.ArrayList;

/**
 * TODO - Make a nice reference to the research team
 * Class sourced from previous research tu_darmstadt etc
 */
public abstract class AuthObject {

    public abstract boolean getResult(ArrayList<CustomPoint> points, float px);

    public abstract Path getPath(float x, float y, float px);

    public abstract String toString();
}