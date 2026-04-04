package com.example.popin;

import android.content.Intent;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import com.google.firebase.database.FirebaseDatabase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class MainActivityTest {

    private MainActivity activity;

    @Before
    public void setUp() {
        // Mock Firebase to prevent initialization errors during Robolectric tests
        try (MockedStatic<FirebaseDatabase> mockedFirebase = mockStatic(FirebaseDatabase.class)) {
            mockedFirebase.when(FirebaseDatabase::getInstance).thenReturn(mock(FirebaseDatabase.class));
            activity = Robolectric.buildActivity(MainActivity.class)
                    .setup()
                    .get();
        }
    }

    @Test
    public void activity_isNotNull() {
        assertNotNull(activity);
    }

    @Test
    public void loginClick_withEmptyFields_showsErrors() {
        TextInputEditText emailInput = activity.findViewById(R.id.EmailInput);
        TextInputEditText passwordInput = activity.findViewById(R.id.passwordInput);
        Button loginButton = activity.findViewById(R.id.loginButton);

        loginButton.performClick();

        // The User.login logic will call onError, which sets errors on these fields
        assertNotNull(emailInput.getError());
    }
}
