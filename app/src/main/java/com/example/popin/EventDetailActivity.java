package com.example.popin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EventDetailActivity extends AppCompatActivity {

    private TextView tvDetailTitle;
    private TextView tvDetailDateTime;
    private TextView tvDetailLocation;
    private TextView tvDetailDetails;

    private Button btnBuyTicket;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_detail);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDateTime = findViewById(R.id.tvDetailDateTime);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailDetails = findViewById(R.id.tvDetailDetails);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        btnBack = findViewById(R.id.btnBack);

        String title = getIntent().getStringExtra("title");
        String dateTime = getIntent().getStringExtra("dateTime");
        String location = getIntent().getStringExtra("location");
        String details = getIntent().getStringExtra("details");

        tvDetailTitle.setText(orFallback(title, "Untitled event"));
        tvDetailDateTime.setText(orFallback(dateTime, "Date unavailable"));
        tvDetailLocation.setText(orFallback(location, "Location unavailable"));
        tvDetailDetails.setText(orFallback(details, "No details provided"));

        btnBuyTicket.setOnClickListener(v -> buyTicket(title));
        btnBack.setOnClickListener(v -> finish());
    }

    private void buyTicket(String title) {
        if (title == null || title.trim().isEmpty()) {
            Toast.makeText(this, "Event title is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userKey = FirebaseUserKey.sanitize(session.getUser().getEmail());
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference("User tickets")
                .child(userKey);

        userTicketsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String existingTicketTitle = ticketSnapshot.child("title").getValue(String.class);
                    if (Objects.equals(existingTicketTitle, title)) {
                        Toast.makeText(EventDetailActivity.this, "You already have this ticket.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Map<String, Object> ticket = new HashMap<>();
                ticket.put("title", title);
                userTicketsRef.push()
                        .setValue(ticket)
                        .addOnSuccessListener(unused ->
                                Toast.makeText(EventDetailActivity.this, "Ticket purchased.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(unused ->
                                Toast.makeText(EventDetailActivity.this, "Purchase failed.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(EventDetailActivity.this, "Purchase failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String orFallback(String value, String fallback) {
        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }
        return value;
    }
}
