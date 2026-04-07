package com.example.popin.EPIC5AT;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.popin.ATHelper.waitForView;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.popin.R;
import com.example.popin.uipages.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AT16CreateEvent {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void AdminCreateEventPaths() {
        onView(withId(R.id.btnReturningUser)).check(matches(isDisplayed()));
        onView(withId(R.id.btnNewUser)).check(matches(isDisplayed()));
        onView(withId(R.id.guestContinue)).check(matches(isDisplayed()));


        onView(withId(R.id.btnReturningUser)).perform(click());

        onView(withId(R.id.login))
                .withFailureHandler((error, viewMatcher) -> {
                })
                .check(matches(isDisplayed()));

        onView(withId(R.id.emailInput)).check(matches(isDisplayed()));

        onView(withId(R.id.emailInput))
                .perform(typeText("admin_user_test@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.password_input)).check(matches(isDisplayed()));

        onView(withId(R.id.password_input))
                .perform(typeText("1234s%"), closeSoftKeyboard());

        onView(withId(R.id.LoginButton)).check(matches(isDisplayed()));

        onView(withId(R.id.LoginButton)).perform(click());
        SystemClock.sleep(2000);

        waitForView(R.id.eventsMain, 5000);

        onView(withId(R.id.EventsTitle)).check(matches(isDisplayed()));


        onView(withId(R.id.nav_admin_dashboard)).perform(click());
        waitForView(R.id.admin_dashboard_main, 5000);

        SystemClock.sleep(2000);
        onView(withId(R.id.AdminDashboardTitle)).check(matches(isDisplayed()));

        onView(withId(R.id.btnCreateEvent)).perform(click());
        SystemClock.sleep(2000);


        onView(withId(R.id.sectionAddEvent)).check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        onView(withId(R.id.editText4))
                .perform(typeText("EVENT TEST"), closeSoftKeyboard());

        onView(withId(R.id.addDateTime))
                .perform(typeText("2026-05-20-12-20"), closeSoftKeyboard());

        onView(withId(R.id.editText2))
                .perform(typeText("H Building Floor 9"), closeSoftKeyboard());

        onView(withId(R.id.editText3))
                .perform(typeText("This is a acceptance test event create"), closeSoftKeyboard());

        onView(withId(R.id.addEvent)).perform(click());
        SystemClock.sleep(2000);


        onView(withId(R.id.etSearchEvents))
                .perform(typeText("EVENT TEST"), closeSoftKeyboard());
        SystemClock.sleep(2000);

        onView(withId(R.id.etSearchEvents))
                .perform(clearText());
        
        onView(withId(R.id.btnCreateEvent)).perform(click());
        SystemClock.sleep(2000);


        onView(withId(R.id.sectionAddEvent)).check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        onView(withId(R.id.editText4))
                .perform(typeText("Add fail"), closeSoftKeyboard());

        onView(withId(R.id.addEvent)).perform(click());
        SystemClock.sleep(2000);


        onView(withId(R.id.addDateTime))
                .perform(typeText("1999-05-20-12-20"), closeSoftKeyboard());

        onView(withId(R.id.editText2))
                .perform(typeText("H Building Floor 9"), closeSoftKeyboard());

        onView(withId(R.id.editText3))
                .perform(typeText("This should fail due to DateTime"), closeSoftKeyboard());


        onView(withId(R.id.addEvent)).perform(click());
        SystemClock.sleep(2000);


        onView(withId(R.id.btnBack)).perform(click());
        SystemClock.sleep(2000);

        onView(withId(R.id.etSearchEvents))
                .perform(typeText("Add fail"), closeSoftKeyboard());
        SystemClock.sleep(2000);

    }

}
