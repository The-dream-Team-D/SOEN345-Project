package com.example.popin;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TicketItemTest {

    @Test
    public void constructor_setsAllFields() {
        TicketItem ticket = new TicketItem("T-123", "Mixer", "Tomorrow", "Lobby");
        assertEquals("T-123", ticket.getTicketId());
        assertEquals("Mixer", ticket.getTitle());
    }

    @Test
    public void emptyConstructor_and_setters() {
        TicketItem ticket = new TicketItem();
        assertNull(ticket.getTicketId());

        ticket.setTicketId("T-456");
        ticket.setTitle("New Event");

        assertEquals("T-456", ticket.getTicketId());
        assertEquals("New Event", ticket.getTitle());
    }
}
