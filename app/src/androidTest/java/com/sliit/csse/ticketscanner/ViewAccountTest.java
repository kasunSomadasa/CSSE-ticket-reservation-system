package com.sliit.csse.ticketscanner;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.test.RenamingDelegatingContext;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by Kasun on 11/20/2017.
 */
public class ViewAccountTest {

    @Rule
    public ActivityTestRule<ViewAccount> activityTestRule = new ActivityTestRule<ViewAccount>(ViewAccount.class,true,false);
    private ViewAccount viewAccountActivity =null;

    @Before
    public void setUp() throws Exception {

        viewAccountActivity=activityTestRule.getActivity();
    }

    @Test
    public void activityLaunch(){

        Intent intent = new Intent();
        intent.putExtra("postId", "Test");
        activityTestRule.launchActivity(intent);
        onView(withId(R.id.loanView));

    }

    @After
    public void tearDown() throws Exception {
        viewAccountActivity=null;
    }

}