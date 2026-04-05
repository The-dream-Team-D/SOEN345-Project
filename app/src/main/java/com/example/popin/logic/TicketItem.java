package com.example.popin.logic;

import android.content.Context;
import android.util.Log;

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
                .getReference("User tickets")
                .child(userKey);

        userTicketsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String existingTitle = ticketSnapshot.child("title").getValue(String.class);
                    if (safeEquals(existingTitle, title)) {
                        callback.onError("Ticket Already Owned");
                        return;
                    }
                }

                DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Event database");

                eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot eventsSnapshot) {
                        for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                            String eventTitle = eventSnapshot.child("title").getValue(String.class);

                            if (safeEquals(eventTitle, title)) {

                                eventSnapshot.getRef().child("attendeeCount").runTransaction(new Transaction.Handler() {
                                    @Override
                                    public Transaction.Result doTransaction(MutableData currentData) {
                                        Integer value = currentData.getValue(Integer.class);
                                        int current = 0;
                                        if (value != null) {
                                            current = value;
                                        }
                                        currentData.setValue(current + 1);
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                                        if (!committed) callback.onError("Failed to update attendance.");
                                    }
                                });
                                break;
                            }
                        }

                        Map<String, Object> ticket = new HashMap<>();
                        ticket.put("title", title);
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



    public static void cancelTicket(String userKey, String title, String ticketId, GenericCallback callback) {
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference("User tickets")
                .child(userKey);

        userTicketsRef.child(ticketId).removeValue().addOnSuccessListener(unused -> {
            DatabaseReference eventsRef = FirebaseDatabase.getInstance().getReference("Event database");

            eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot eventsSnapshot) {
                    for (DataSnapshot eventSnapshot : eventsSnapshot.getChildren()) {
                        String eventTitle = eventSnapshot.child("title").getValue(String.class);

                        if (safeEquals(eventTitle, title)) {

                            eventSnapshot.getRef().child("attendeeCount").runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData currentData) {
                                    Integer value = currentData.getValue(Integer.class);
                                    int current = 0;
                                    if (value != null) {
                                        current = value;
                                    }
                                    currentData.setValue(Math.max(0, current - 1));
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                                    if (!committed) callback.onError("Failed to update attendance.");
                                }
                            });
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
        }).addOnFailureListener(unused -> callback.onError("Cancellation failed."));
    }



}

