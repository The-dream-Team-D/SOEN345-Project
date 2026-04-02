package com.example.popin.logic;

public class EventItem {
    private String title;
    private String dateTime;
    private String location;
    private String details;
    private String imgURL;

    // Required by Firebase
    public EventItem() {}

    public EventItem(String title, String dateTime, String location) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.details = "";
    }

    public EventItem(String title, String dateTime, String location, String details) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.details = details;
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

}