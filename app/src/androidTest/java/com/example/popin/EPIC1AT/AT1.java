package com.example.popin.EPIC1AT;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.popin.R;
import com.example.popin.UIpages.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AT1 {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void MainActivityRendersAllElementsCorrectly() {

        onView(withId(R.id.wave_image)).check(matches(isDisplayed()));
        onView(withId(R.id.partyPopImage)).check(matches(isDisplayed()));
        onView(withId(R.id.balloonsImage)).check(matches(isDisplayed()));

        onView(withId(R.id.Logo)).check(matches(isDisplayed()));
        onView(withId(R.id.loginPhrase)).check(matches(isDisplayed()));
        onView(withId(R.id.Welcome)).check(matches(isDisplayed()));

        onView(withId(R.id.btnReturningUser)).check(matches(isDisplayed()));
        onView(withId(R.id.btnNewUser)).check(matches(isDisplayed()));
        onView(withId(R.id.guestContinue)).check(matches(isDisplayed()));
    }
}
