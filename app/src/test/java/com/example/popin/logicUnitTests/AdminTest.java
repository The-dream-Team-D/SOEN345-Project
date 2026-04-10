package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.popin.logic.Admin;
import com.example.popin.logic.EventCatalog;
import com.example.popin.logic.EventCategory;
import com.example.popin.logic.EventItem;

import org.junit.After;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class AdminTest {

    private MockedStatic<EventCatalog> mockedEventCatalog;

    @After
    public void tearDown() {
        if (mockedEventCatalog != null) {
            mockedEventCatalog.close();
        }
    }

    @Test
    public void constructorAndUserAccessors_workCorrectly() {
        Admin admin = new Admin("admin@example.com", "secret");

        assertNotNull(admin);
        assertEquals("admin@example.com", admin.getEmail());
        assertEquals("secret", admin.getPassword());

    }

    @Test
    public void addEvent_delegatesToEventCatalog() {
        EventCatalog mockCatalog = mock(EventCatalog.class);
        EventCatalog.EventActionCallback callback = mock(EventCatalog.EventActionCallback.class);
        long date = System.currentTimeMillis() + 60_000;

        mockedEventCatalog = Mockito.mockStatic(EventCatalog.class);
        mockedEventCatalog.when(EventCatalog::getInstance).thenReturn(mockCatalog);

        Admin admin = new Admin("admin@example.com", "secret");
        admin.addEvent("SOEN Mixer", "EV Building", "Networking", date, callback);

        verify(mockCatalog, times(1)).addEvent(any(EventItem.class), eq(callback));
    }

    @Test
    public void removeEvent_delegatesToEventCatalog() {
        EventCatalog mockCatalog = mock(EventCatalog.class);
        EventCatalog.EventActionCallback callback = mock(EventCatalog.EventActionCallback.class);

        mockedEventCatalog = Mockito.mockStatic(EventCatalog.class);
        mockedEventCatalog.when(EventCatalog::getInstance).thenReturn(mockCatalog);

        Admin admin = new Admin("admin@example.com", "secret");
        admin.removeEvent("SOEN Mixer", callback);

        verify(mockCatalog).deleteEventByName("SOEN Mixer", callback);
    }

    @Test
    public void updateEvent_delegatesToEventCatalog() {
        EventCatalog mockCatalog = mock(EventCatalog.class);
        EventCatalog.EventActionCallback callback = mock(EventCatalog.EventActionCallback.class);
        long newDate = System.currentTimeMillis() + 120_000;

        mockedEventCatalog = Mockito.mockStatic(EventCatalog.class);
        mockedEventCatalog.when(EventCatalog::getInstance).thenReturn(mockCatalog);

        Admin admin = new Admin("admin@example.com", "secret");
        admin.updateEvent(
                "Old Event",
                "New Event",
                "Hall A",
                "Updated details",
                newDate,
                EventCategory.SOCIAL,
                120,
                callback
        );

        verify(mockCatalog).updateEventByName(
                eq("Old Event"),
                any(EventCatalog.EventUpdateRequest.class),
                eq(callback)
        );
    }
}

