package com.example.popin.activityUnitTests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.popin.R;
import com.example.popin.reusableui.BackButtonComponentView;
import com.example.popin.uipages.MainActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class BackButtonComponentViewTest {

    @Test
    public void backButtonGoesToMainActivity() {

        AppCompatActivity mockHostActivity = Robolectric.buildActivity(AppCompatActivity.class).create().get();
        ShadowActivity mockShadowActivity = shadowOf(mockHostActivity);
        BackButtonComponentView backButton = new BackButtonComponentView(mockHostActivity, null);

        backButton.findViewById(R.id.backButton).performClick();

        Intent started = mockShadowActivity.getNextStartedActivity();
        assertNotNull("Expected an Intent to be started", started);
        assertEquals(MainActivity.class.getName(),
                started.getComponent().getClassName());
    }

}
