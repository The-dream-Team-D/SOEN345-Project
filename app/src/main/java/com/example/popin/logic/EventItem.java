package com.example.popin.logic;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventItem {
    private String title;
    private long dateTime;
    private String location;
    private String details;
    private String imgURL;

    private int capacity;
    private int attendeeCount;

    private EventCategory category;

    // Required by Firebase
    public EventItem() {}

    public EventItem(String title, long dateTime, String location) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.details = "";
        this.imgURL = "";
        this.capacity = 100;
        this.attendeeCount = 0;
        this.category = EventCategory.Entertainment;
    }

    public EventItem(String title, long dateTime, String location, String details) {
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.details = details;
        this.capacity = 100;
        this.attendeeCount = 0;
        this.category = EventCategory.Educational;
    }
    public EventItem(String title, long dateTime, String location, String details, String imgURL, int capacity, EventCategory category) {
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

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public boolean setDateTime(int year, int month, int day, int hour, int minute) {


        Long result = convertTimeToLong(year, month, day, hour, minute);

        if(result == -1){
            return false;
        }
        else{
            this.dateTime = result;
            return true;
        }
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


    public static long convertTimeToLong(int year, int month, int day, int hour, int minute){
        if (year < 1970) return -1;
        if (month < 0 || month > 11) return -1;
        if (day < 1 || day > 31) return -1;
        if (hour < 0 || hour > 23) return -1;
        if (minute < 0 || minute > 59) return -1;

        Calendar calendar = Calendar.getInstance();

        calendar.setLenient(false);

        try {
            calendar.set(year, month, day, hour, minute);

            return calendar.getTimeInMillis();

        } catch (Exception e) {
            return -1;
        }

    }

    public static String FormatTime(long dateTime) {

        Date date = new Date(dateTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy - h:mm a", Locale.US);
        return sdf.format(date);
    }

}