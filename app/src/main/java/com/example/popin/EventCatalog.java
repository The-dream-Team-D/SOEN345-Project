package com.example.popin;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class EventCatalog {
    private static EventCatalog instance;
    private final DatabaseReference eventsRef;

    private EventCatalog() {
        eventsRef = FirebaseDatabase.getInstance().getReference("Events");
    }

    public static EventCatalog getInstance() {
        if (instance == null) {
            instance = new EventCatalog();
        }
        return instance;
    }

    /** Clears singleton; for unit tests only. */
    static void clearInstanceForTests() {
        instance = null;
    }

    public interface EventActionCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    private void findEventSnapshotByName(String eventName,
                                         EventActionCallback callback,
                                         Consumer<DataSnapshot> onFound) {
        if (callback == null || onFound == null) {
            return;
        }

        if (eventName == null || eventName.trim().isEmpty()) {
            callback.onError("Event name is empty");
            return;
        }

        Query query = eventsRef.orderByChild("name").equalTo(eventName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError("No event found with that name");
                    return;
                }

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    onFound.accept(eventSnapshot);
                    break;
                }

                callback.onError("No event found with that name");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }

    public void addEvent(Event event, EventActionCallback callback) {
        if (callback == null) {
            return;
        }

        if (event == null) {
            callback.onError("Event is null");
            return;
        }

        if (event.getName() == null || event.getName().trim().isEmpty()) {
            callback.onError("Event name is empty");
            return;
        }

        eventsRef.child(String.valueOf(event.getId()))
                .setValue(event)
                .addOnSuccessListener(unused ->
                        callback.onSuccess("Event added successfully"))
                .addOnFailureListener(e ->
                        callback.onError("Failed to add event: " + e.getMessage()));
    }

    public void deleteEventByName(String eventName, EventActionCallback callback) {
        if (callback == null) {
            return;
        }

        findEventSnapshotByName(eventName, callback, eventSnapshot ->
                eventSnapshot.getRef().removeValue()
                        .addOnSuccessListener(unused ->
                                callback.onSuccess("Event deleted successfully"))
                        .addOnFailureListener(e ->
                                callback.onError("Failed to delete event: " + e.getMessage()))
        );
    }

    public void editEventByName(String eventName, Event updatedEvent, EventActionCallback callback) {
        if (callback == null) {
            return;
        }

        if (updatedEvent == null) {
            callback.onError("Updated event is null");
            return;
        }

        findEventSnapshotByName(eventName, callback, eventSnapshot -> {
            Event existingEvent = eventSnapshot.getValue(Event.class);
            if (existingEvent == null) {
                callback.onError("Failed to read event data");
                return;
            }

            updatedEvent.setId(existingEvent.getId());
            eventSnapshot.getRef().setValue(updatedEvent)
                    .addOnSuccessListener(unused ->
                            callback.onSuccess("Event updated successfully"))
                    .addOnFailureListener(e ->
                            callback.onError("Failed to update event: " + e.getMessage()));
        });
    }

    public void updateEventByName(String currentEventName,
                                  String newName,
                                  String newLocation,
                                  String newDescription,
                                  java.util.Date newDate,
                                  EventCategory newCategory,
                                  Boolean newAvailability,
                                  EventActionCallback callback) {
        if (callback == null) {
            return;
        }

        findEventSnapshotByName(currentEventName, callback, eventSnapshot -> {
            Map<String, Object> updates = new HashMap<>();

            if (newName != null && !newName.trim().isEmpty()) {
                updates.put("name", newName);
            }

            if (newLocation != null && !newLocation.trim().isEmpty()) {
                updates.put("location", newLocation);
            }

            if (newDescription != null && !newDescription.trim().isEmpty()) {
                updates.put("description", newDescription);
            }

            if (newDate != null) {
                updates.put("date", newDate);
            }

            if (newCategory != null) {
                updates.put("eventCategory", newCategory);
            }

            if (newAvailability != null) {
                updates.put("isAvailable", newAvailability);
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
        });
    }
}

