package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.example.popin.FirebaseTestAssistant;
import com.example.popin.logic.EventCatalog;
import com.example.popin.logic.EventCategory;
import com.example.popin.logic.EventItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

public class EventCatalogTest {

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
    public void addEvent_nullEvent_returnsErrorImmediately() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.addEvent(null, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Event is null", error.get());
    }

    @Test
    public void addEvent_emptyName_returnsErrorImmediately() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();
        EventItem event = new EventItem("   ", System.currentTimeMillis(), "Hall A", "Details");

        catalog.addEvent(event, new EventCatalog.EventActionCallback() {
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
    public void deleteEventByName_emptyName_returnsErrorImmediately() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.deleteEventByName("   ", new EventCatalog.EventActionCallback() {
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
    public void editEventByName_nullUpdatedEvent_returnsErrorImmediately() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();

        catalog.editEventByName("SOEN Mixer", null, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                fail("Expected error");
            }

            @Override
            public void onError(String message) {
                error.set(message);
            }
        });

        assertEquals("Updated event is null", error.get());
    }

    @Test
    public void updateEventByName_emptyCurrentName_returnsErrorImmediately() {
        EventCatalog catalog = EventCatalog.getInstance();
        AtomicReference<String> error = new AtomicReference<>();
        EventCatalog.EventUpdateRequest request = new EventCatalog.EventUpdateRequest();
        request.setNewName("New name");
        request.setNewLocation("New location");
        request.setNewDescription("New description");
        request.setNewDate(System.currentTimeMillis());
        request.setNewCategory(EventCategory.SOCIAL);
        request.setNewCapacity(100);

        catalog.updateEventByName(
                " ",
                request,
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        fail("Expected error");
                    }

                    @Override
                    public void onError(String message) {
                        error.set(message);
                    }
                }
        );

        assertEquals("Event name is empty", error.get());
    }
}

