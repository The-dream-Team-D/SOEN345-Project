package com.example.popin;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EventItemTest {

    @Test
    public void constructor_setsAllFields() {
        EventItem item = new EventItem("SOEN Mixer", "March 20, 2026 - 6:00 PM", "EV Building Lobby");

        assertEquals("SOEN Mixer", item.getTitle());
        assertEquals("March 20, 2026 - 6:00 PM", item.getDateTime());
        assertEquals("EV Building Lobby", item.getLocation());
    }

    @Test
    public void emptyConstructor_and_setters() {
        EventItem item = new EventItem();
        assertNull(item.getTitle());

        item.setTitle("New Title");
        item.setDateTime("2024-01-01");
        item.setLocation("Montreal");

        assertEquals("New Title", item.getTitle());
        assertEquals("2024-01-01", item.getDateTime());
        assertEquals("Montreal", item.getLocation());
    }
}
