package com.example.popin;

import static org.junit.Assert.assertEquals;

import android.view.View;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 34)
public class MyTicketsActivityHelpersTest {

    @Test
    public void sanitizeKey_replacesInvalidFirebaseCharacters() throws Exception {
        MyTicketsActivity activity = new MyTicketsActivity();
        Method sanitize = MyTicketsActivity.class.getDeclaredMethod("sanitizeKey", String.class);
        sanitize.setAccessible(true);

        assertEquals("unknown_user", sanitize.invoke(activity, " "));
        assertEquals("john_doe_mail_com___", sanitize.invoke(activity, "john.doe#mail/com[$]"));
    }

    @Test
    public void normalize_trimsAndLowercases() throws Exception {
        MyTicketsActivity activity = new MyTicketsActivity();
        Method normalize = MyTicketsActivity.class.getDeclaredMethod("normalize", String.class);
        normalize.setAccessible(true);

        assertEquals("", normalize.invoke(activity, new Object[]{null}));
        assertEquals("soen mixer", normalize.invoke(activity, "  SOEN Mixer  "));
    }

    @Test
    public void updateEmptyState_setsVisibleWhenAdapterIsEmpty() throws Exception {
        MyTicketsActivity activity = new MyTicketsActivity();

        Field adapterField = MyTicketsActivity.class.getDeclaredField("ticketAdapter");
        adapterField.setAccessible(true);
        adapterField.set(activity, new TicketAdapter(Collections.emptyList(), t -> {}, t -> {}));

        Field emptyField = MyTicketsActivity.class.getDeclaredField("emptyStateText");
        emptyField.setAccessible(true);
        TextView empty = new TextView(ApplicationProvider.getApplicationContext());
        empty.setVisibility(View.GONE);
        emptyField.set(activity, empty);

        Method update = MyTicketsActivity.class.getDeclaredMethod("updateEmptyState");
        update.setAccessible(true);
        update.invoke(activity);

        assertEquals(View.VISIBLE, empty.getVisibility());
    }
}
