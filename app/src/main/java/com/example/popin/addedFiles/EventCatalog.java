package com.example.popin.addedFiles;

import com.example.popin.logic.EventCategory;
import com.example.popin.logic.EventItem;
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

    public void editEventByName(String eventName, Event updatedEvent, EventActionCallback callback) {
        Query query = queryByEventName(eventName, callback);
        if (query == null) {
            return;
        }

        if (updatedEvent == null) {
            callback.onError("Updated event is null");
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(NO_EVENT_FOUND_ERROR);
                    return;
                }

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event existingEvent = eventSnapshot.getValue(Event.class);

                    if (existingEvent == null) {
                        callback.onError("Failed to read event data");
                        return;
                    }

                    updatedEvent.setId(existingEvent.getId());

                    if (!updatedEvent.isAvailable()) {
                        updatedEvent.setAvailable(existingEvent.isAvailable());
                    }

                    eventSnapshot.getRef().setValue(updatedEvent)
                            .addOnSuccessListener(unused ->
                                    callback.onSuccess("Event updated successfully"))
                            .addOnFailureListener(e ->
                                    callback.onError("Failed to update event: " + e.getMessage()));
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(DB_ERROR_PREFIX + error.getMessage());
            }
        });
    }

    public void updateEventByName(String currentEventName,
                                  String newName,
                                  String newLocation,
                                  String newDescription,
                                  long newDate,
                                  EventCategory newCategory,
                                  int newCapacity,
                                  EventActionCallback callback) {

        Query query = queryByEventName(currentEventName, callback);
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

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Map<String, Object> updates = new HashMap<>();

                    if (newName != null && !newName.trim().isEmpty()) {
                        updates.put("title", newName);
                    }

                    if (newLocation != null && !newLocation.trim().isEmpty()) {
                        updates.put("location", newLocation);
                    }

                    if (newDescription != null && !newDescription.trim().isEmpty()) {
                        updates.put("details", newDescription);
                    }

                    if (newDate != -1 ) {
                        updates.put("dateTime", newDate);
                    }

                    if (newCategory != null)  {
                        updates.put("category", newCategory);
                    }

                    if (newCapacity != -1) {
                        updates.put("capacity", newCapacity);
                    }

                    if (updates.isEmpty()) {
                        callback.onError("No fields provided to update");
                        return;
                    }

                    eventSnapshot.getRef().updateChildren(updates)
                            .addOnSuccessListener(unused ->
                                    callback.onSuccess("Event updated successfully"))
                            .addOnFailureListener(e ->
                                    callback.onError("Failed to update event: " + e.getMessage()));
                    return;

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(DB_ERROR_PREFIX + error.getMessage());
            }
        });
    }
}

