package com.example.popin.UIpages;

import static com.example.popin.logic.Notifications.sendNotification;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.popin.logic.GenericCallback;
import com.example.popin.logic.NotificationType;
import com.example.popin.logic.TicketItem;
import com.example.popin.logic.UserInSession;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EventDetailActivity extends AppCompatActivity {

    private TextView tvDetailTitle;
    private TextView tvDetailDateTime;
    private TextView tvDetailLocation;
    private TextView tvDetailDetails;
    private TextView tvEventCategory;
    private TextView tvEventAttendance;
    private ImageView eventImageURL;

    private Button btnBuyTicket;
    private Button btnTicketPurchased;
    private Button btnCapacityReached;

    private Button btnBack;
    private Button btnLoginToBuy;
    private int eventCapacity;
    private int eventAttendance;

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
        boolean isFull = getIntent().getIntExtra("capacity", 0) <= getIntent().getIntExtra("attendees", 0);

        TicketItem.buyTicket(userKey, title, new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(EventDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                setupTicketButton(true, isFull);
                sendNotification(session.getUser(), title, NotificationType.RegisterEvent);

                tvEventAttendance.setText("Tickets sold: " + (eventAttendance +1) + " out of " + eventCapacity + " available tickets!");
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EventDetailActivity.this, message, Toast.LENGTH_SHORT).show();
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

        eventCapacity = getIntent().getIntExtra("capacity", 100);
        eventAttendance = getIntent().getIntExtra("attendees", 0);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailDateTime = findViewById(R.id.tvDetailDateTime);
        tvDetailLocation = findViewById(R.id.tvDetailLocation);
        tvDetailDetails = findViewById(R.id.tvDetailDetails);

        tvEventCategory = findViewById(R.id.tvEventCategory);
        tvEventAttendance = findViewById(R.id.tvEventAttendance);


        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        btnTicketPurchased = findViewById(R.id.btnTicketPurchased);
        btnCapacityReached = findViewById(R.id.btnCapacityReached);

        btnBack = findViewById(R.id.btnBack);

        eventImageURL = findViewById(R.id.EventImage);

        String title = getIntent().getStringExtra("title");
        String dateTime = getIntent().getStringExtra("dateTime");
        String location = getIntent().getStringExtra("location");
        String details = getIntent().getStringExtra("details");
        String imgURL = getIntent().getStringExtra("imgURL");
        String eventCategory = getIntent().getStringExtra("eventCategory");




        tvDetailTitle.setText(title);
        tvDetailDateTime.setText(dateTime);
        tvDetailLocation.setText(location);
        tvDetailDetails.setText(details);
        tvEventCategory.setText(eventCategory);

        tvEventAttendance.setText("Tickets sold: " + eventAttendance + " out of " + eventCapacity + " available tickets!");


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


    private void setupTicketButton(boolean ticketAlreadyBought, boolean attendanceFull) {
        if (ticketAlreadyBought) {
            btnBuyTicket.setVisibility(View.GONE);
            btnTicketPurchased.setVisibility(View.VISIBLE);
            btnCapacityReached.setVisibility(View.GONE);

        } else {
            if(attendanceFull){
                btnCapacityReached.setVisibility(View.VISIBLE);
                btnBuyTicket.setVisibility(View.GONE);

            }
            else{
                btnBuyTicket.setVisibility(View.VISIBLE);
                btnTicketPurchased.setVisibility(View.GONE);
                btnCapacityReached.setVisibility(View.GONE);
            }

        }

        btnLoginToBuy.setVisibility(View.GONE);

    }

    private void checkRegistrationStatus(String title) {
        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            btnBuyTicket.setVisibility(View.GONE);
            btnTicketPurchased.setVisibility(View.GONE);
            btnCapacityReached.setVisibility(View.GONE);
            btnLoginToBuy.setVisibility(View.VISIBLE);
            return;
        }

        String userKey = sanitizeKey(session.getUser().getEmail());
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference("User tickets")
                .child(userKey);

        boolean isFull = getIntent().getIntExtra("capacity", 0) <= getIntent().getIntExtra("attendees", 0);

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

                setupTicketButton(isRegistered, isFull);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                setupTicketButton(false, isFull);
            }
        });
    }


}
