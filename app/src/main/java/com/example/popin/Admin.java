package com.example.popin;

import java.util.Date;

public class Admin extends User {
    private String id;

    public Admin(String email, String password, String id) {
        super(email, password);
        this.id = id;
        setIsAdmin(true);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addEvent(String name,
                         String location,
                         String description,
                         Date date,
                         EventCategory eventCategory,
                         EventCatalog.EventActionCallback callback) {
        Event event = new Event(name, location, description, date, eventCategory);
        EventCatalog.getInstance().addEvent(event, callback);
    }

    public void removeEvent(String eventName, EventCatalog.EventActionCallback callback) {
        EventCatalog.getInstance().deleteEventByName(eventName, callback);
    }

    public void updateEvent(String currentEventName,
                            String newName,
                            String newLocation,
                            String newDescription,
                            Date newDate,
                            EventCategory newCategory,
                            Boolean newAvailability,
                            EventCatalog.EventActionCallback callback) {
        EventCatalog.getInstance().updateEventByName(
                currentEventName,
                newName,
                newLocation,
                newDescription,
                newDate,
                newCategory,
                newAvailability,
                callback
        );
    }
}
