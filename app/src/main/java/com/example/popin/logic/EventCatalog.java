package com.example.popin.logic;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class EventCatalog {
    private static EventCatalog instance;
    private final DatabaseReference eventsRef;
    private static final String EVENT_NAME_EMPTY_ERROR = "Event name is empty";
    private static final String NO_EVENT_FOUND_ERROR = "No event found with that name";
    private static final String DB_ERROR_PREFIX = "Database error: ";

    private EventCatalog() {
        eventsRef = FirebaseDatabase.getInstance().getReference("Event database");
    }

    public static EventCatalog getInstance() {
        if (instance == null) {
            instance = new EventCatalog();
        }
        return instance;
    }

    public interface EventActionCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    public static class EventUpdateRequest {
        private String newName;
        private String newLocation;
        private String newDescription;
        private long newDate = -1L;
        private EventCategory newCategory;
        private int newCapacity = -1;

        public String getNewName() { return newName; }
        public void setNewName(String newName) { this.newName = newName; }
        public String getNewLocation() { return newLocation; }
        public void setNewLocation(String newLocation) { this.newLocation = newLocation; }
        public String getNewDescription() { return newDescription; }
        public void setNewDescription(String newDescription) { this.newDescription = newDescription; }
        public long getNewDate() { return newDate; }
        public void setNewDate(long newDate) { this.newDate = newDate; }
        public EventCategory getNewCategory() { return newCategory; }
        public void setNewCategory(EventCategory newCategory) { this.newCategory = newCategory; }
        public int getNewCapacity() { return newCapacity; }
        public void setNewCapacity(int newCapacity) { this.newCapacity = newCapacity; }
    }

    private Query queryByEventName(String eventName, EventActionCallback callback) {
        if (eventName == null || eventName.trim().isEmpty()) {
            callback.onError(EVENT_NAME_EMPTY_ERROR);
            return null;
        }
        return eventsRef.orderByChild("title").equalTo(eventName);
    }

    public void addEvent(EventItem event, EventActionCallback callback) {
        if (event == null) {
            callback.onError("Event is null");
            return;
        }

        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            callback.onError(EVENT_NAME_EMPTY_ERROR);
            return;
        }

        eventsRef.push()
                .setValue(event)
                .addOnSuccessListener(unused ->
                        callback.onSuccess("Event added successfully"))
                .addOnFailureListener(e ->
                        callback.onError("Failed to add event: " + e.getMessage()));
    }

    public void deleteEventByName(String eventName, EventActionCallback callback) {
        Query query = queryByEventName(eventName, callback);
        if (query == null) {
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(NO_EVENT_FOUND_ERROR);
                    return;
                }

                DataSnapshot firstEvent = snapshot.getChildren().iterator().next();
                firstEvent.getRef().removeValue()
                        .addOnSuccessListener(unused ->
                                callback.onSuccess("Event deleted successfully"))
                        .addOnFailureListener(e ->
                                callback.onError("Failed to delete event: " + e.getMessage()));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(DB_ERROR_PREFIX + error.getMessage());
            }
        });
    }

    public void updateEventByName(String currentEventName,
                                  EventUpdateRequest request,
                                  EventActionCallback callback) {

        Query query = queryByEventName(currentEventName, callback);
        if (query == null) {
            return;
        }

        if (request == null) {
            callback.onError("No fields provided to update");
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(NO_EVENT_FOUND_ERROR);
                    return;
                }

                Map<String, Object> updates = buildUpdates(request);
                if (updates.isEmpty()) {
                    callback.onError("No fields provided to update");
                    return;
                }

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    eventSnapshot.getRef().updateChildren(updates)
                            .addOnSuccessListener(unused ->
                                    callback.onSuccess("Event updated successfully"))
                            .addOnFailureListener(e ->
                                    callback.onError("Failed to update event: " + e.getMessage()));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(DB_ERROR_PREFIX + error.getMessage());
            }
        });
    }

    private Map<String, Object> buildUpdates(EventUpdateRequest request) {
        Map<String, Object> updates = new HashMap<>();
        if (request.getNewName() != null && !request.getNewName().trim().isEmpty()) {
            updates.put("title", request.getNewName());
        }
        if (request.getNewLocation() != null && !request.getNewLocation().trim().isEmpty()) {
            updates.put("location", request.getNewLocation());
        }
        if (request.getNewDescription() != null && !request.getNewDescription().trim().isEmpty()) {
            updates.put("details", request.getNewDescription());
        }
        if (request.getNewDate() != -1) {
            updates.put("dateTime", request.getNewDate());
        }
        if (request.getNewCategory() != null) {
            updates.put("category", request.getNewCategory());
        }
        if (request.getNewCapacity() != -1) {
            updates.put("capacity", request.getNewCapacity());
        }
        return updates;
    }
}


