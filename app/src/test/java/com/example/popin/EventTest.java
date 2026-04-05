package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.example.popin.addedFiles.Event;
import com.example.popin.logic.EventCategory;

import org.junit.Test;

import java.util.Date;

public class EventTest {

    @Test
    public void constructor_setsFields_andDefaultsToAvailable() {
        Date date = new Date();
        Event event = new Event("SOEN Mixer", "EV Building", "Networking event", date, EventCategory.SOCIAL);

        assertEquals("SOEN Mixer", event.getName());
        assertEquals("EV Building", event.getLocation());
        assertEquals("Networking event", event.getDescription());
        assertEquals(date, event.getDate());
        assertEquals(EventCategory.SOCIAL, event.getEventCategory());
        assertTrue(event.isAvailable());
    }

    @Test
    public void constructor_assignsIncreasingIds() {
        Event first = new Event("A", "L1", "D1", new Date(), EventCategory.EDUCATIONAL);
        Event second = new Event("B", "L2", "D2", new Date(), EventCategory.SOCIAL);

        assertNotEquals(first.getId(), second.getId());
        assertTrue(second.getId() > first.getId());
    }

    @Test
    public void setters_updateFields() {
        Event event = new Event();
        Date date = new Date();

        event.setId(77);
        event.setName("Updated Event");
        event.setLocation("Hall A");
        event.setDescription("Updated details");
        event.setDate(date);
        event.setEventCategory(EventCategory.SPORTS);
        event.setAvailable(false);

        assertEquals(77, event.getId());
        assertEquals("Updated Event", event.getName());
        assertEquals("Hall A", event.getLocation());
        assertEquals("Updated details", event.getDescription());
        assertEquals(date, event.getDate());
        assertEquals(EventCategory.SPORTS, event.getEventCategory());
        assertFalse(event.isAvailable());
    }
}

