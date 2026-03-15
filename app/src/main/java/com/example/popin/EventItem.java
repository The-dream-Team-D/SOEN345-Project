package com.example.popin;

public class EventItem {
    private String title;
    private String dateTime;
    private String location;

    // Required by Firebase
    public EventItem() {}

    public EventItem(String title, String dateTime, String location) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
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
