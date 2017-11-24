package com.sliit.csse.ticketscanner;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/20/2017.
 */
public class StartRideTest {

    @Rule
    public ActivityTestRule<StartRide> activityTestRule = new ActivityTestRule<StartRide>(StartRide.class,true,false);
    private StartRide StartRideActivity =null;


    @Before
    public void setUp() throws Exception {
        StartRideActivity=activityTestRule.getActivity();
    }

    @Test
    public void activityLaunch(){

        Intent intent = new Intent();
        intent.putExtra("postId", "Test");
        activityTestRule.launchActivity(intent);
        onView(withId(R.id.journey));

    }

    @After
    public void tearDown() throws Exception {
        StartRideActivity=null;
    }

}