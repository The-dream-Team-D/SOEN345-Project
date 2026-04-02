package com.example.popin.UIpages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.popin.R;
import com.example.popin.logic.NotificationType;
import com.example.popin.logic.Notifications;
import com.example.popin.logic.UserInSession;
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
    private ImageView eventImageURL;

    private Button btnBuyTicket;
    private Button btnTicketPurchased;

    private Button btnBack;
    private Button btnLoginToBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_detail);
        setup();

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
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(EventDetailActivity.this, "Ticket purchased.", Toast.LENGTH_SHORT).show();
                            setupTicketButton(true);
                            Notifications.sendEmailNotification(session.getUser(), getIntent().getStringExtra("title"), NotificationType.RegisterEvent);
                        })
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

    private void setup() {

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDateTime = findViewById(R.id.tvDetailDateTime);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailDetails = findViewById(R.id.tvDetailDetails);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        btnTicketPurchased = findViewById(R.id.btnTicketPurchased);

        btnBack = findViewById(R.id.btnBack);

        eventImageURL = findViewById(R.id.EventImage);

        String title = getIntent().getStringExtra("title");
        String dateTime = getIntent().getStringExtra("dateTime");
        String location = getIntent().getStringExtra("location");
        String details = getIntent().getStringExtra("details");
        String imgURL = getIntent().getStringExtra("imgURL");

        tvDetailTitle.setText(title);
        tvDetailDateTime.setText(dateTime);
        tvDetailLocation.setText(location);
        tvDetailDetails.setText(details);


        Glide.with(this)
                .load(imgURL)
                .override(300, 200)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.img_placeholder)
                .into(eventImageURL);

        btnLoginToBuy = findViewById(R.id.btnLoginToBuy);
        btnLoginToBuy.setOnClickListener(v ->
                startActivity(new Intent(this, LogInActivity.class)));


        checkRegistrationStatus(title);

        btnBuyTicket.setOnClickListener(v -> buyTicket(title, dateTime, location));
        btnBack.setOnClickListener(v -> finish());
    }


    private void setupTicketButton(boolean ticketAlreadyBought) {
        if (ticketAlreadyBought) {
            btnBuyTicket.setVisibility(View.GONE);
            btnTicketPurchased.setVisibility(View.VISIBLE);
            btnLoginToBuy.setVisibility(View.GONE);

        } else {
            btnBuyTicket.setVisibility(View.VISIBLE);
            btnTicketPurchased.setVisibility(View.GONE);
            btnLoginToBuy.setVisibility(View.GONE);
        }
    }

    private void checkRegistrationStatus(String title) {
        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            btnBuyTicket.setVisibility(View.GONE);
            btnTicketPurchased.setVisibility(View.GONE);
            btnLoginToBuy.setVisibility(View.VISIBLE);
            return;
        }

        String userKey = sanitizeKey(session.getUser().getEmail());
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference("User tickets")
                .child(userKey);

        userTicketsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean isRegistered = false;
                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String existingTicketTitle = ticketSnapshot.child("title").getValue(String.class);
                    if (safeEquals(existingTicketTitle, title)) {
                        isRegistered = true;
                        break;
                    }
                }
                setupTicketButton(isRegistered);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                setupTicketButton(false);
            }
        });
    }


}
