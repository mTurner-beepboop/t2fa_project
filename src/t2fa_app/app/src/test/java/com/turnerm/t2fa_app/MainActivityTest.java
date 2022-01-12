package com.turnerm.t2fa_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import junit.framework.TestCase;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void checkTestWorking(){
        assertEquals(1,1);
    }

    @Test
    public void appropriateDelay_isCorrect(){
        //Test time returned if system time is before midnight but after cutoff
        assertEquals(UtilityFuncs.findAppropriateDelay(22), 60 * 60 * 1000 * 10);
        //Test time returned if system time is after midnight but before social hours
        assertEquals(UtilityFuncs.findAppropriateDelay(2), 60 * 60 * 1000 * 6);

        //Test time returned if system time is in social hours
        assertTrue(UtilityFuncs.findAppropriateDelay(12) >= 60*60*1000*3);
        assertTrue(UtilityFuncs.findAppropriateDelay(12) <= 60*60*1000*5);

        //Test time returned if System is in social hours, but delay is outside
        assertEquals(UtilityFuncs.findAppropriateDelay(20), 60*60*1000*12);
    }
}