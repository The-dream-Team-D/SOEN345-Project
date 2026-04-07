package com.example.popin.EPIC1AT;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.popin.R;
import com.example.popin.uipages.MainActivity;

import org.junit.Rule;
import org.junit.Test;

public class AT3ResetPassword {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    // TEST WILL FAIL SINCE THE RESET CODE IS NOT HARDCODED TO A CERTAIN VALUE AS IT WAS IN RECORDING


    @Test
    public void UserLogin() {
        onView(withId(R.id.wave_image)).check(matches(isDisplayed()));
        onView(withId(R.id.partyPopImage)).check(matches(isDisplayed()));
        onView(withId(R.id.balloonsImage)).check(matches(isDisplayed()));

        onView(withId(R.id.Logo)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPhrase)).check(matches(isDisplayed()));
        onView(withId(R.id.Welcome)).check(matches(isDisplayed()));

        onView(withId(R.id.btnReturningUser)).check(matches(isDisplayed()));
        onView(withId(R.id.btnNewUser)).check(matches(isDisplayed()));
        onView(withId(R.id.guestContinue)).check(matches(isDisplayed()));


        onView(withId(R.id.btnReturningUser)).perform(click());

        onView(withId(R.id.login))
                .withFailureHandler((error, viewMatcher) -> {})
                .check(matches(isDisplayed()));

        onView(withId(R.id.emailInput)).check(matches(isDisplayed()));

        onView(withId(R.id.forgotPassword)).perform(click());

        onView(withId(R.id.forgotPassword))
                .withFailureHandler((error, viewMatcher) -> {})
                .check(matches(isDisplayed()));

        SystemClock.sleep(2000);

        onView(withId(R.id.emailInput)).check(matches(isDisplayed()));

        onView(withId(R.id.emailInput))
                .perform(typeText("+14875732933"), closeSoftKeyboard());

        onView(withId(R.id.password_input)).check(matches(isDisplayed()));

        onView(withId(R.id.password_input))
                .perform(typeText("NewPassword11"), closeSoftKeyboard());

        onView(withId(R.id.eye_password_icon)).perform(click());

        SystemClock.sleep(2000);

        onView(withId(R.id.sendResetCodeButton)).perform(click());

        SystemClock.sleep(4000);

        onView(withId(R.id.AfterEmailCheck))
                .check(matches(isDisplayed()));

        onView(withId(R.id.resetCodeInput)).check(matches(isDisplayed()));

        onView(withId(R.id.resetCodeInput))
                .perform(typeText("T42E"), closeSoftKeyboard());

        SystemClock.sleep(2000);

        onView(withId(R.id.ChangePasswordButton)).perform(click());

        SystemClock.sleep(2000);

        onView(withId(R.id.login))
                .withFailureHandler((error, viewMatcher) -> {})
                .check(matches(isDisplayed()));

        SystemClock.sleep(2000);

        onView(withId(R.id.emailInput)).check(matches(isDisplayed()));

        onView(withId(R.id.emailInput))
                .perform(typeText("+14875732933"), closeSoftKeyboard());

        onView(withId(R.id.password_input)).check(matches(isDisplayed()));

        onView(withId(R.id.password_input))
                .perform(typeText("NewPassword11"), closeSoftKeyboard());

        onView(withId(R.id.eye_password_icon)).perform(click());

        onView(withId(R.id.LoginButton)).check(matches(isDisplayed()));

        onView(withId(R.id.LoginButton)).perform(click());
        SystemClock.sleep(2000);

        onView(withId(R.id.eventsMain))
                .withFailureHandler((error, viewMatcher) -> {})
                .check(matches(isDisplayed()));

        onView(withId(R.id.EventsTitle)).check(matches(isDisplayed()));


    }
}
