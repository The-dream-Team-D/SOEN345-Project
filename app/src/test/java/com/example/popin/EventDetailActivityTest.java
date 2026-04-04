package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Method;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class EventDetailActivityTest {

    @After
    public void tearDown() {
        UserInSession.clear();
    }

    @Test
    public void onCreate_displaysIntentExtras() {
        Intent intent = new Intent();
        intent.putExtra("title", "SOEN Mixer");
        intent.putExtra("dateTime", "March 20, 2026 - 6:00 PM");
        intent.putExtra("location", "EV Building Lobby");
        intent.putExtra("details", "Networking event");

        EventDetailActivity activity = Robolectric.buildActivity(EventDetailActivity.class, intent)
                .setup()
                .get();

        TextView title = activity.findViewById(R.id.tvDetailTitle);
        TextView dateTime = activity.findViewById(R.id.tvDetailDateTime);
        TextView location = activity.findViewById(R.id.tvDetailLocation);
        TextView details = activity.findViewById(R.id.tvDetailDetails);

        assertEquals("SOEN Mixer", title.getText().toString());
        assertEquals("March 20, 2026 - 6:00 PM", dateTime.getText().toString());
        assertEquals("EV Building Lobby", location.getText().toString());
        assertEquals("Networking event", details.getText().toString());
    }

    @Test
    public void backButtonClick_finishesActivity() {
        EventDetailActivity activity = Robolectric.buildActivity(EventDetailActivity.class)
                .setup()
                .get();

        Button backButton = activity.findViewById(R.id.btnBack);
        backButton.performClick();

        assertTrue(activity.isFinishing());
    }

    @Test
    public void safeEquals_returnsExpectedValues() throws Exception {
        EventDetailActivity activity = Robolectric.buildActivity(EventDetailActivity.class)
                .setup()
                .get();

        Method method = EventDetailActivity.class.getDeclaredMethod("safeEquals", String.class, String.class);
        method.setAccessible(true);

        assertTrue((boolean) method.invoke(activity, null, null));
        assertTrue((boolean) method.invoke(activity, "x", "x"));
        assertTrue(!(boolean) method.invoke(activity, null, "x"));
        assertTrue(!(boolean) method.invoke(activity, "x", "y"));
    }

    @Test
    public void sanitizeKey_replacesFirebaseInvalidCharacters() throws Exception {
        EventDetailActivity activity = Robolectric.buildActivity(EventDetailActivity.class)
                .setup()
                .get();

        Method method = EventDetailActivity.class.getDeclaredMethod("sanitizeKey", String.class);
        method.setAccessible(true);

        assertEquals("unknown_user", method.invoke(activity, "   "));
        assertEquals("john_doe_mail_com___", method.invoke(activity, "john.doe#mail/com[$]"));
    }
}
