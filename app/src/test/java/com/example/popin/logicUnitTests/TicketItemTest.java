package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.popin.logic.EventItem;
import com.example.popin.logic.TicketItem;

import org.junit.Test;

public class TicketItemTest {

    @Test
    public void constructor_setsAllFields() {
        long dateTime = EventItem.convertTimeToLong(2026, 2, 20, 18, 0);
        TicketItem item = new TicketItem(
                "1",
                "SOEN Mixer",
                dateTime,
                "EV Building Lobby"
        );

        assertEquals("1", item.getTicketId());
        assertEquals("SOEN Mixer", item.getTitle());
        assertEquals(dateTime, item.getDateTime());
        assertEquals("EV Building Lobby", item.getLocation());
    }

    @Test
    public void emptyConstructor_and_setters() {
        TicketItem item = new TicketItem();

        assertNull(item.getTicketId());

        item.setTicketId("2");
        item.setTitle("New Title");
        item.setDateTime(2024, 0, 1, 6, 0);
        item.setLocation("Montreal");

        assertEquals("2", item.getTicketId());
        assertEquals("New Title", item.getTitle());
        assertEquals(EventItem.convertTimeToLong(2024, 0, 1, 6, 0), item.getDateTime());
        assertEquals("Montreal", item.getLocation());
    }

}
