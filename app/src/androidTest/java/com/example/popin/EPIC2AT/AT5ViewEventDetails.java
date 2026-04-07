package com.example.popin.EPIC2AT;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.popin.ATHelper.waitForView;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.popin.R;
import com.example.popin.uipages.MainActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AT5ViewEventDetails {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void A_viewEventsSuccess(){
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
                .perform(typeText("+14875732933"), closeSoftKeyboard());

        onView(withId(R.id.password_input)).check(matches(isDisplayed()));

        onView(withId(R.id.password_input))
                .perform(typeText("1234s%"), closeSoftKeyboard());

        onView(withId(R.id.eye_password_icon)).perform(click());

        onView(withId(R.id.LoginButton)).check(matches(isDisplayed()));

        onView(withId(R.id.LoginButton)).perform(click());
        SystemClock.sleep(2000);

        waitForView(R.id.eventsMain, 5000);

        onView(withId(R.id.EventsTitle)).check(matches(isDisplayed()));

        onView(withText("SOEN Mixer"))
                .perform(click());
        SystemClock.sleep(2000);

        onView(withId(R.id.btnBuyTicket)).perform(click());

        SystemClock.sleep(2000);
    }

    @Test
    public void B_viewEventsFail(){
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
                .perform(typeText("acceptance_user@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.password_input)).check(matches(isDisplayed()));

        onView(withId(R.id.password_input))
                .perform(typeText("1234s%"), closeSoftKeyboard());

        onView(withId(R.id.eye_password_icon)).perform(click());

        onView(withId(R.id.LoginButton)).check(matches(isDisplayed()));

        onView(withId(R.id.LoginButton)).perform(click());
        SystemClock.sleep(2000);

        waitForView(R.id.eventsMain, 5000);

        onView(withId(R.id.EventsTitle)).check(matches(isDisplayed()));

        SystemClock.sleep(2000);

    }
}
