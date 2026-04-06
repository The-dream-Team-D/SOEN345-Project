package com.example.popin.logic;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TicketItem extends EventItem {
    private static final String TITLE_KEY = "title";
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


    public static void buyTicket(String userKey, String title, GenericCallback callback) {
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference(USER_TICKETS)
                .child(userKey);

        userTicketsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String existingTitle = ticketSnapshot.child(TITLE_KEY).getValue(String.class);
                    if (safeEquals(existingTitle, title)) {
                        callback.onError("Ticket Already Owned");
                        return;
                    }
                }

                DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference(EVENT_DATABASE);

                eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot eventsSnapshot) {
                        for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                            String eventTitle = eventSnapshot.child(TITLE_KEY).getValue(String.class);

                            if (safeEquals(eventTitle, title)) {
                                adjustAttendeeCount(eventSnapshot.getRef().child("attendeeCount"), 1, callback);
                                break;
                            }
                        }

                        Map<String, Object> ticket = new HashMap<>();
                        ticket.put(TITLE_KEY, title);
                        userTicketsRef.push()
                                .setValue(ticket)
                                .addOnSuccessListener(unused -> callback.onSuccess("Ticket Bought successfully"))
                                .addOnFailureListener(unused -> callback.onError("Purchase failed."));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        callback.onError("Failed to load events.");
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Purchase failed.");
            }
        });
    }

    private static boolean safeEquals(String a, String b) {
        if (a == null || b == null) return false;
        return a.trim().equalsIgnoreCase(b.trim());
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

    private static void updateAttendeeCountForEvent(String title, int delta, GenericCallback callback) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference(EVENT_DATABASE);
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot eventsSnapshot) {
                for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                    String eventTitle = eventSnapshot.child(TITLE_KEY).getValue(String.class);
                    if (safeEquals(eventTitle, title)) {
                        adjustAttendeeCount(eventSnapshot.getRef().child("attendeeCount"), delta, callback);
                        break;
                    }
                }
                callback.onSuccess("Ticket cancelled successfully");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Failed to load events.");
            }
        });
    }

    public static void cancelTicket(String userKey, String title, String ticketId, GenericCallback callback) {
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference(USER_TICKETS)
                .child(userKey);

        userTicketsRef.child(ticketId).removeValue()
                .addOnSuccessListener(unused -> updateAttendeeCountForEvent(title, -1, callback))
                .addOnFailureListener(unused -> callback.onError("Cancellation failed."));
    }



}

