package com.example.popin;

public class EventItem {
    private final String title;
    private final String dateTime;
    private final String location;

    public EventItem(String title, String dateTime, String location) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }
}
