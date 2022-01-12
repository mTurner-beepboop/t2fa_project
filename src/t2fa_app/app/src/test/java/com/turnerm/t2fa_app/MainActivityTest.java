package com.turnerm.t2fa_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
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

    // TODO - This seems to require some more knowledge to do well due to the context requirement, stack overflow suggests a wrapper class for the Notification class so it can be mocked with Mockito. Basically, it just needs some knowledge I don't have yet, here's the stackoverflow link: https://stackoverflow.com/questions/44334429/how-to-make-android-unit-test-for-notifications
    /**
    @Test
    public void createNotification_isCorrect(){
        assertSame(UtilityFuncs.createNotification());
    }
    **/
}