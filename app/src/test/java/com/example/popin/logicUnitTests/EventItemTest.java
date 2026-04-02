package com.example.popin.logicUnitTests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.popin.logic.EventItem;

public class EventItemTest {

    @Test
    public void constructor_setsAllFields() {
        EventItem item = new EventItem(
                "SOEN Mixer",
                "March 20, 2026 - 6:00 PM",
                "EV Building Lobby",
                "Meet other SOEN students, network, and enjoy snacks."
        );

        assertEquals("SOEN Mixer", item.getTitle());
        assertEquals("March 20, 2026 - 6:00 PM", item.getDateTime());
        assertEquals("EV Building Lobby", item.getLocation());
        assertEquals("Meet other SOEN students, network, and enjoy snacks.", item.getDetails());
    }

    @Test
    public void emptyConstructor_and_setters() {
        EventItem item = new EventItem();

        assertNull(item.getTitle());

        item.setTitle("New Title");
        item.setDateTime("2024-01-01");
        item.setLocation("Montreal");
        item.setDetails("Sample event description");

        assertEquals("New Title", item.getTitle());
        assertEquals("2024-01-01", item.getDateTime());
        assertEquals("Montreal", item.getLocation());
        assertEquals("Sample event description", item.getDetails());
    }
}