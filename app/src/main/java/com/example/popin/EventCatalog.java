package com.example.popin;

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

    private EventCatalog() {
        eventsRef = FirebaseDatabase.getInstance().getReference("Events");
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

    String eventError = "Event name is empty";
    String noEventFoundWithNameError = "No event found with that name";
    String DBErrorTag = "Database error: ";
    public void addEvent(Event event, EventActionCallback callback) {
        if (event == null) {
            callback.onError("Event is null");
            return;
        }

        if (event.getName() == null || event.getName().trim().isEmpty()) {
            callback.onError(eventError);
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
        if (eventName == null || eventName.trim().isEmpty()) {
            callback.onError(eventError);
            return;
        }

        Query query = eventsRef.orderByChild("name").equalTo(eventName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(noEventFoundWithNameError);
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
                callback.onError(DBErrorTag + error.getMessage());
            }
        });
    }

    public void editEventByName(String eventName, Event updatedEvent, EventActionCallback callback) {
        if (eventName == null || eventName.trim().isEmpty()) {
            callback.onError(eventError);
            return;
        }

        if (updatedEvent == null) {
            callback.onError("Updated event is null");
            return;
        }

        Query query = eventsRef.orderByChild("name").equalTo(eventName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(noEventFoundWithNameError);
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

                    DataSnapshot firstEvent = snapshot.getChildren().iterator().next();
                    firstEvent.getRef().setValue(updatedEvent)
                            .addOnSuccessListener(unused ->
                                    callback.onSuccess("Event updated successfully"))
                            .addOnFailureListener(e ->
                                    callback.onError("Failed to update event: " + e.getMessage()));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(DBErrorTag + error.getMessage());
            }
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

        if (currentEventName == null || currentEventName.trim().isEmpty()) {
            callback.onError(eventError);
            return;
        }

        Query query = eventsRef.orderByChild("name").equalTo(currentEventName);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(noEventFoundWithNameError);
                    return;
                }

                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
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

                    DataSnapshot firstEvent = snapshot.getChildren().iterator().next();
                    firstEvent.getRef().updateChildren(updates)
                            .addOnSuccessListener(unused ->
                                    callback.onSuccess("Event updated successfully"))
                            .addOnFailureListener(e ->
                                    callback.onError("Failed to update event: " + e.getMessage()));

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(DBErrorTag + error.getMessage());
            }
        });
    }
}

