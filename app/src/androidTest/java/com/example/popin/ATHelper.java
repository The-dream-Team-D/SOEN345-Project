package com.example.popin;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.SystemClock;

public class ATHelper {

    public static void waitForView(int viewId, int timeoutMs) {
        long end = SystemClock.elapsedRealtime() + timeoutMs;
        while (SystemClock.elapsedRealtime() < end) {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()));
                return;
            } catch (Exception ignored) {
                SystemClock.sleep(300);
            }
        }
        onView(withId(viewId)).check(matches(isDisplayed()));
    }
}
