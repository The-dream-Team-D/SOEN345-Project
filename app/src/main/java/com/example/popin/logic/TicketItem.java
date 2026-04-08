package com.example.popin.logic;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TicketItem extends EventItem {
    private static final String EVENTID_KEY = "eventID";
    private static final String USERID_KEY = "userID";

    private static final String EVENT_DATABASE = "Event database";
    private static final String USER_TICKETS = "User tickets";
    private String ticketId;

    public TicketItem() {
        super();
    }

    public TicketItem(String ticketId, String title, long dateTime, String location) {
        super(title, dateTime, location);
        this.ticketId = ticketId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }


    public static void buyTicket(String userId, String eventId, GenericCallback callback) {

        DatabaseReference ticketsRef = FirebaseDatabase.getInstance().getReference(USER_TICKETS);

        Query userTicketsQuery = ticketsRef.orderByChild(USERID_KEY).equalTo(userId);

        userTicketsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String existingEventId = ticketSnapshot.child(EVENTID_KEY).getValue(String.class);

                    if (eventId.equals(existingEventId)) {
                        callback.onError("Ticket Already Owned");
                        return;
                    }
                }

                Map<String, Object> ticket = new HashMap<>();
                ticket.put(USERID_KEY, userId);
                ticket.put(EVENTID_KEY, eventId);

                ticketsRef.push().setValue(ticket)
                        .addOnSuccessListener(unused -> callback.onSuccess("Ticket Bought successfully"))
                        .addOnFailureListener(e -> callback.onError("Purchase failed."));

                DatabaseReference eventRef = FirebaseDatabase.getInstance()
                        .getReference(EVENT_DATABASE)
                        .child(eventId);
                adjustAttendeeCount(eventRef.child("attendeeCount"), 1, null);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Purchase failed.");
            }
        });
    }

    private static void adjustAttendeeCount(DatabaseReference ref, int delta, GenericCallback callback) {
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData currentData) {
                Integer value = currentData.getValue(Integer.class);
                int current = value != null ? value : 0;
                currentData.setValue(Math.max(0, current + delta));
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                if (!committed) callback.onError("Failed to update attendance.");
            }
        });
    }

    private static void updateAttendeeCountForEvent(String eventId, int delta, GenericCallback callback) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference(EVENT_DATABASE);

        DatabaseReference eventRef = eventsRef.child(eventId);

        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onError("Failed to load events.");
                    return;
                }

                adjustAttendeeCount(eventRef.child("attendeeCount"), delta, callback);

                callback.onSuccess("Ticket cancelled successfully");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Failed to load events.");
            }
        });
    }

    public static void cancelTicket(String eventID, String ticketId, GenericCallback callback) {
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference(USER_TICKETS);

        userTicketsRef.child(ticketId).removeValue()
                .addOnSuccessListener(unused -> updateAttendeeCountForEvent(eventID, -1, callback))
                .addOnFailureListener(unused -> callback.onError("Cancellation failed."));
    }

}

