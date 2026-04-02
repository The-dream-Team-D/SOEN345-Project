package com.example.popin.logic;

public class EventItem {
    private String title;
    private String dateTime;
    private String location;
    private String details;
    private String imgURL;

    private int capacity;
    private int attendeeCount;

    private EventCategory category;

    // Required by Firebase
    public EventItem() {}

    public EventItem(String title, String dateTime, String location) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.details = "";
        this.imgURL = "";
        this.capacity = 100;
        this.attendeeCount = 0;
        this.category = EventCategory.Entertainment;
    }

    public EventItem(String title, String dateTime, String location, String details, String imgURL, int capacity, EventCategory category) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.details = details;
        this.imgURL = imgURL;
        this.capacity = capacity;
        this.attendeeCount = 0;
        this.category = category;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImgURL() {
        return imgURL;
    }
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }
    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }

    public EventCategory getCategory() {
        return category;
    }
    public void setCategory(EventCategory category) {
        this.category = category;
    }

}