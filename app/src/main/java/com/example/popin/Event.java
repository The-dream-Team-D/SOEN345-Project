package com.example.popin;

import java.util.Date;

public class Event {
    private static int idCounter = 0;
    private int id;
    private String name, location, description;
    private Date date;
    private boolean isAvailable;
    private EventCategory eventCategory;

    public Event() {
        // Required empty constructor for Firebase
    }

    public Event(String name, String location, String description, Date date, EventCategory eventCategory){
        this.id = ++idCounter;
        this.name = name;
        this.location = location;
        this.description = description;
        this.date = date;
        this.eventCategory = eventCategory;
        this.isAvailable = true;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }
}