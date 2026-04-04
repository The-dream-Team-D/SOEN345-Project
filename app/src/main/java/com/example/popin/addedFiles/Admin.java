package com.example.popin.addedFiles;

import com.example.popin.logic.EventCategory;
import com.example.popin.logic.User;
import com.example.popin.logic.EventItem;
import java.util.Date;

public class Admin extends User {
    private String id;

    public Admin(String email, String password, String id) {
        super(email, password);
        this.id = id;
    }

    public Admin(User user) {
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setAddress(user.getAddress());
        this.setBio(user.getBio());
        this.setIsAdmin(true);
        this.setUserNotificationPreference(user.getUserNotificationPreference());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addEvent(String name, String location, String description, long date,
                         EventCatalog.EventActionCallback callback) {
        EventItem event = new EventItem(name, date, location, description);
        EventCatalog.getInstance().addEvent(event, callback);
    }

    public void removeEvent(String eventName, EventCatalog.EventActionCallback callback) {
        EventCatalog.getInstance().deleteEventByName(eventName, callback);
    }

    public void updateEvent(String currentEventName,
                            String newName,
                            String newLocation,
                            String newDescription,
                            long newDate,
                            EventCategory newCategory,
                            int newCapacity,
                            EventCatalog.EventActionCallback callback) {
        EventCatalog.getInstance().updateEventByName(
                currentEventName,
                newName,
                newLocation,
                newDescription,
                newDate,
                newCategory,
                newCapacity,
                callback
        );
    }
}