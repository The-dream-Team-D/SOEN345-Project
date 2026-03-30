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

        tvDetailTitle.setText(title);
        tvDetailDateTime.setText(dateTime);
        tvDetailLocation.setText(location);
        tvDetailDetails.setText(details);

        btnBuyTicket.setOnClickListener(v -> buyTicket(title, dateTime, location));
        btnBack.setOnClickListener(v -> finish());
    }

    private void buyTicket(String title, String dateTime, String location) {
        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userKey = sanitizeKey(session.getUser().getEmail());
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference("User tickets")
                .child(userKey);

        userTicketsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String existingTicketTitle = ticketSnapshot.child("title").getValue(String.class);
                    if (safeEquals(existingTicketTitle, title)) {
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

    private boolean safeEquals(String left, String right) {
        if (left == null) {
            return right == null;
        }
        return left.equals(right);
    }

    private String sanitizeKey(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "unknown_user";
        }
        return raw
                .replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_")
                .replace("/", "_");
    }
}
