package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.example.popin.addedfiles.EventCatalog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

public class EventCatalogValidationExtraTest {

    @Before
    public void setUp() throws Exception {
        resetSingleton();
        FirebaseTestAssistant.setupMockFirebase();
    }

    @After
    public void tearDown() throws Exception {
        FirebaseTestAssistant.tearDown();
        resetSingleton();
    }

    private void resetSingleton() throws Exception {
        Field instanceField = EventCatalog.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    @Test
    public void editEventByName_emptyName_returnsErrorImmediately() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.editEventByName("   ", null, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Event name is empty", error.get());
    }

    @Test
    public void deleteEventByName_nullName_returnsErrorImmediately() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.deleteEventByName(null, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Event name is empty", error.get());
    }
}

