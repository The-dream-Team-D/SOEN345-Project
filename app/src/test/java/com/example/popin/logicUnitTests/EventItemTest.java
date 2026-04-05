package com.example.popin.logicUnitTests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.popin.logic.EventCategory;
import com.example.popin.logic.EventItem;

public class EventItemTest {

    @Test
    public void constructor_setsAllFields() {
        EventItem item = new EventItem(
                "SOEN Mixer",
                EventItem.convertTimeToLong(2026, 2, 20, 18, 0),
                "EV Building Lobby",
                "Meet other SOEN students, network, and enjoy snacks in a casual social setting.",
                "https://images.stockcake.com/public/9/6/d/96d4100c-ca71-4e09-b84e-d7e90c294a87_large/joyful-party-celebration-stockcake.jpg",
                100,
                EventCategory.Social
        );

        assertEquals("SOEN Mixer", item.getTitle());
        assertEquals(EventItem.convertTimeToLong(2026, 2, 20, 18, 0), item.getDateTime());
        assertEquals("EV Building Lobby", item.getLocation());
        assertEquals("Meet other SOEN students, network, and enjoy snacks in a casual social setting.", item.getDetails());
    }

    @Test
    public void emptyConstructor_and_setters() {
        EventItem item = new EventItem();

        assertNull(item.getTitle());

        item.setTitle("New Title");
        item.setDateTime(2024, 0, 1, 12, 0);
        item.setLocation("Montreal");
        item.setDetails("Sample event description");

        assertEquals("New Title", item.getTitle());
        assertEquals(EventItem.convertTimeToLong(2024, 0, 1, 12, 0), item.getDateTime());
        assertEquals("Montreal", item.getLocation());
        assertEquals("Sample event description", item.getDetails());
    }
}
