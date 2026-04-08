package com.example.popin.uipages;

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
    private static final String EXTRA_CAPACITY = "capacity";
    private static final String EXTRA_ATTENDEES = "attendees";

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

    private void buyTicket(String title, String eventID) {
        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userKey = session.getUser().getUserID();
        boolean isFull = getIntent().getIntExtra(EXTRA_CAPACITY, 0) <= getIntent().getIntExtra(EXTRA_ATTENDEES, 0);

        TicketItem.buyTicket(userKey, eventID, new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(EventDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                setupTicketButton(true, isFull);
                sendNotification(session.getUser(), title, NotificationType.REGISTER_EVENT, "");

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

        eventCapacity = getIntent().getIntExtra(EXTRA_CAPACITY, 100);
        eventAttendance = getIntent().getIntExtra(EXTRA_ATTENDEES, 0);

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

        String eventID = getIntent().getStringExtra("EventID");
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


        checkRegistrationStatus(eventID);

        btnBuyTicket.setOnClickListener(v -> buyTicket(title, eventID));
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

    private void checkRegistrationStatus(String eventId) {
        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            btnBuyTicket.setVisibility(View.GONE);
            btnTicketPurchased.setVisibility(View.GONE);
            btnCapacityReached.setVisibility(View.GONE);
            btnLoginToBuy.setVisibility(View.VISIBLE);
            return;
        }


        String userKey = session.getUser().getUserID();
        DatabaseReference userTicketsRef = FirebaseDatabase.getInstance()
                .getReference("User tickets");

        boolean isFull = getIntent().getIntExtra(EXTRA_CAPACITY, 0) <= getIntent().getIntExtra(EXTRA_ATTENDEES, 0);

        userTicketsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean isRegistered = false;

                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String ticketUserId = ticketSnapshot.child("userID").getValue(String.class);
                    String ticketEventId = ticketSnapshot.child("eventID").getValue(String.class);

                    if (userKey.equals(ticketUserId) && eventId.equals(ticketEventId)) {
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

