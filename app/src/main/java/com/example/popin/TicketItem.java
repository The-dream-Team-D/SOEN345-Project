package com.example.popin;

public class TicketItem {
    private String ticketId;
    private String title;
    private String dateTime;
    private String location;

    public TicketItem() {
    }

    public TicketItem(String ticketId, String title, String dateTime, String location) {
        this.ticketId = ticketId;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
