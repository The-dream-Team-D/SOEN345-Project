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
public class AT17EditEvent {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void AdminEditEventPaths() {
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


        onView(withId(R.id.etSearchEvents))
                .perform(typeText("EVENT TEST"), closeSoftKeyboard());
        SystemClock.sleep(3000);


        onView(withId(R.id.etSearchEvents))
                .perform(clearText());

        onView(withId(R.id.btnUpdateEvent)).perform(click());
        SystemClock.sleep(2000);


        onView(withId(R.id.sectionUpdateEvent)).check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        onView(withId(R.id.editTextCurrentEventName))
                .perform(typeText("EVENT TEST"), closeSoftKeyboard());

        onView(withId(R.id.editText9))
                .perform(typeText("NEW EVENT TITLE"), closeSoftKeyboard());

        onView(withId(R.id.button3)).perform(click());
        SystemClock.sleep(2000);


        onView(withId(R.id.etSearchEvents))
                .perform(typeText("EVENT TEST"), closeSoftKeyboard());
        SystemClock.sleep(2000);

        onView(withId(R.id.etSearchEvents))
                .perform(clearText());

        onView(withId(R.id.etSearchEvents))
                .perform(typeText("NEW EVENT TITLE"), closeSoftKeyboard());
        SystemClock.sleep(2000);

        onView(withId(R.id.etSearchEvents))
                .perform(clearText());


        onView(withId(R.id.btnUpdateEvent)).perform(click());
        SystemClock.sleep(2000);


        onView(withId(R.id.sectionUpdateEvent)).check(matches(isDisplayed()));
        SystemClock.sleep(2000);

        onView(withId(R.id.editTextCurrentEventName))
                .perform(typeText("EVENT TEST"), closeSoftKeyboard());

        onView(withId(R.id.editText9))
                .perform(typeText("Should fail, no event by that name now"), closeSoftKeyboard());

        onView(withId(R.id.button3)).perform(click());
        SystemClock.sleep(2000);

        onView(withId(R.id.editTextCurrentEventName))
                .perform(clearText());

        onView(withId(R.id.editText9))
                .perform(clearText());

        onView(withId(R.id.editTextCurrentEventName))
                .perform(typeText("NEW EVENT TITLE"), closeSoftKeyboard());

        onView(withId(R.id.editDateTime))
                .perform(typeText("Not proper date time, will fail"), closeSoftKeyboard());

        onView(withId(R.id.button3)).perform(click());
        SystemClock.sleep(2000);




        onView(withId(R.id.btnBack)).perform(click());
        SystemClock.sleep(2000);

        onView(withId(R.id.etSearchEvents))
                .perform(typeText("NEW EVENT TITLE"), closeSoftKeyboard());
        SystemClock.sleep(2000);

    }
}
