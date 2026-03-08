package com.example.popin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EventItemTest {

    @Test
    public void constructor_setsAllFields() {
        EventItem item = new EventItem("SOEN Mixer", "March 20, 2026 - 6:00 PM", "EV Building Lobby");

        assertEquals("SOEN Mixer", item.getTitle());
        assertEquals("March 20, 2026 - 6:00 PM", item.getDateTime());
        assertEquals("EV Building Lobby", item.getLocation());
    }
}
