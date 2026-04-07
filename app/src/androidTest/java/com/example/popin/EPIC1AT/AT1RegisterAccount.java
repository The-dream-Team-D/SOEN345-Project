package com.example.popin.EPIC1AT;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.example.popin.ATHelper.waitForView;
import static org.hamcrest.Matchers.allOf;

import android.os.SystemClock;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.popin.R;
import com.example.popin.uipages.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AT1RegisterAccount {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);


    // RUN CLASS NOT TESTS SINGULARLY
    @Test
    public void RegisterAccountSuccessPath1() {

        onView(withId(R.id.wave_image)).check(matches(isDisplayed()));
        onView(withId(R.id.partyPopImage)).check(matches(isDisplayed()));
        onView(withId(R.id.balloonsImage)).check(matches(isDisplayed()));

        onView(withId(R.id.Logo)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPhrase)).check(matches(isDisplayed()));
        onView(withId(R.id.Welcome)).check(matches(isDisplayed()));

        onView(withId(R.id.btnReturningUser)).check(matches(isDisplayed()));
        onView(withId(R.id.btnNewUser)).check(matches(isDisplayed()));
        onView(withId(R.id.guestContinue)).check(matches(isDisplayed()));

        onView(withId(R.id.btnNewUser)).perform(click());

        onView(withId(R.id.NameInput))
                .perform(typeText("ACCEPTANCETEST"), closeSoftKeyboard());

        onView(withId(R.id.emailInput))
                .perform(typeText("acceptance_user@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.password_input))
                .perform(typeText("1234s%"), closeSoftKeyboard());

        onView(withId(R.id.eye_password_icon)).perform(click());

        onView(withId(R.id.SignUpButton)).perform(click());

        waitForView(R.id.login, 5000);



    }

    @Test
    public void RegisterAccountSuccessPath2() {

        onView(withId(R.id.wave_image)).check(matches(isDisplayed()));
        onView(withId(R.id.partyPopImage)).check(matches(isDisplayed()));
        onView(withId(R.id.balloonsImage)).check(matches(isDisplayed()));

        onView(withId(R.id.Logo)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPhrase)).check(matches(isDisplayed()));
        onView(withId(R.id.Welcome)).check(matches(isDisplayed()));

        onView(withId(R.id.btnReturningUser)).check(matches(isDisplayed()));
        onView(withId(R.id.btnNewUser)).check(matches(isDisplayed()));
        onView(withId(R.id.guestContinue)).check(matches(isDisplayed()));
        onView(withId(R.id.btnNewUser)).perform(click());

        waitForView(R.id.signUp, 5000);

        onView(withId(R.id.NameInput))
                .perform(typeText("AcceptanceTester2"), closeSoftKeyboard());

        onView(withId(R.id.emailInput))
                .perform(typeText("+14875732933"), closeSoftKeyboard());

        onView(withId(R.id.password_input))
                .perform(typeText("1234s%"), closeSoftKeyboard());

        onView(withId(R.id.eye_password_icon)).perform(click());

        onView(withId(R.id.SignUpButton)).perform(click());

        waitForView(R.id.login, 5000);
    }



    @Test
    public void RegisterAccountFailurePaths() {

        onView(withId(R.id.wave_image)).check(matches(isDisplayed()));
        onView(withId(R.id.partyPopImage)).check(matches(isDisplayed()));
        onView(withId(R.id.balloonsImage)).check(matches(isDisplayed()));

        onView(withId(R.id.Logo)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPhrase)).check(matches(isDisplayed()));
        onView(withId(R.id.Welcome)).check(matches(isDisplayed()));

        onView(withId(R.id.btnReturningUser)).check(matches(isDisplayed()));
        onView(withId(R.id.btnNewUser)).check(matches(isDisplayed()));
        onView(withId(R.id.guestContinue)).check(matches(isDisplayed()));


        onView(withId(R.id.btnNewUser)).perform(click());

        waitForView(R.id.signUp, 5000);


        onView(withId(R.id.NameInput))
                .perform(typeText("ACCEPTANCETEST"), closeSoftKeyboard());

        onView(withId(R.id.emailInput))
                .perform(typeText("acceptance_user@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.password_input))
                .perform(typeText("1234s%"), closeSoftKeyboard());

        onView(withId(R.id.eye_password_icon)).perform(click());

        onView(withId(R.id.SignUpButton)).perform(click());

        onView(withId(R.id.emailInput)).perform(click());

        SystemClock.sleep(2000);

        onView(withId(R.id.emailInput)).perform(clearText());

        SystemClock.sleep(1000);

        onView(withId(R.id.emailInput))
                .perform(typeText("acceptance_user.com"), closeSoftKeyboard());

        SystemClock.sleep(1000);

        onView(withId(R.id.SignUpButton)).perform(click());

        SystemClock.sleep(1000);

        onView(withId(R.id.emailInput)).perform(click());



    }
}

