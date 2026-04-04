package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TicketItemTest {

    @Test
    public void constructor_setsTicketIdAndInheritedFields() {
        TicketItem item = new TicketItem("ticket-1", "SOEN Mixer", "March 20, 2026 - 6:00 PM", "EV Building Lobby");

        assertEquals("ticket-1", item.getTicketId());
        assertEquals("SOEN Mixer", item.getTitle());
        assertEquals("March 20, 2026 - 6:00 PM", item.getDateTime());
        assertEquals("EV Building Lobby", item.getLocation());
    }

    @Test
    public void emptyConstructor_startsWithNullTicketId() {
        TicketItem item = new TicketItem();

        assertNull(item.getTicketId());
    }

    @Test
    public void setTicketId_updatesValue() {
        TicketItem item = new TicketItem();
        item.setTicketId("ticket-99");

        assertEquals("ticket-99", item.getTicketId());
    }
}
