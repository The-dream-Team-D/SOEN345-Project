package com.example.popin;

public class TicketItem extends EventItem {
    private String ticketId;

    public TicketItem() {
        super();
    }

    public TicketItem(String ticketId, String title, String dateTime, String location) {
        super(title, dateTime, location);
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }
}
