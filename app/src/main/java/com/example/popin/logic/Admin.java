package com.example.popin.logic;

public class Admin extends User {

    public Admin(String email, String password) {
        super(email, password);
    }

    public Admin(User user) {
        this.setUserID(user.getUserID());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setPhoneNumber(user.getPhoneNumber());
        this.setAddress(user.getAddress());
        this.setBio(user.getBio());
        this.setIsAdmin(true);
        this.setUserNotificationPreference(user.getUserNotificationPreference());
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
        EventCatalog.EventUpdateRequest request = new EventCatalog.EventUpdateRequest();
        request.setNewName(newName);
        request.setNewLocation(newLocation);
        request.setNewDescription(newDescription);
        request.setNewDate(newDate);
        request.setNewCategory(newCategory);
        request.setNewCapacity(newCapacity);
        EventCatalog.getInstance().updateEventByName(
                currentEventName,
                request,
                callback
        );
    }
}
