package com.example.popin.uipages;

import static com.example.popin.logic.EventItem.formatTime;
import static com.example.popin.logic.Notifications.sendNotification;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.popin.R;
import com.example.popin.logic.EventFilterDateType;
import com.example.popin.logic.EventItem;
import com.example.popin.logic.GenericCallback;
import com.example.popin.logic.NotificationType;
import com.example.popin.logic.TicketAdapter;
import com.example.popin.logic.TicketItem;
import com.example.popin.logic.UserInSession;
import com.example.popin.reusableui.NavBarComponentView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

public class MyTicketsActivity extends AppCompatActivity {
    private TicketAdapter ticketAdapter;
    private final List<TicketItem> ticketList = new ArrayList<>();
    private DatabaseReference userTicketsRef;
    private DatabaseReference eventsRef;
    private TextView emptyStateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tickets);

        RecyclerView recyclerView = findViewById(R.id.rvTickets);
        EditText searchInput = findViewById(R.id.etSearchEvents);

        Button loginButton = findViewById(R.id.LoginToGetTickets);

        View navBar = findViewById(R.id.bottomNav);
        NavBarComponentView.setup(navBar);

        ConstraintLayout loggedInUser = findViewById(R.id.LoggedInUser);
        ConstraintLayout loggedOutUser = findViewById(R.id.LoggedOutUser);

        Button pastTickets = findViewById(R.id.PastReservations);
        Button upcomingTickets = findViewById(R.id.UpcomingReservations);

        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            loggedInUser.setVisibility(View.GONE);

            loggedOutUser.setVisibility(View.VISIBLE);

            loginButton.setOnClickListener(v ->
                    startActivity(new Intent(this, LogInActivity.class)));
        } else {

            loggedInUser.setVisibility(View.VISIBLE);
            loggedOutUser.setVisibility(View.GONE);


            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            emptyStateText = findViewById(R.id.tvTicketsEmptyState);
            ticketAdapter = new TicketAdapter(ticketList, this::cancelReservation, this::openTicketDetails);
            recyclerView.setAdapter(ticketAdapter);

            String userKey = session.getUser().getUserID();
            userTicketsRef = FirebaseDatabase.getInstance()
                    .getReference("User tickets");
            eventsRef = FirebaseDatabase.getInstance().getReference("Event database");

            fetchTickets(userKey);

            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* no-op */ }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ticketAdapter.filter(s.toString());
                    updateEmptyState();
                }

                @Override
                public void afterTextChanged(Editable s) { /* no-op */ }
            });

            pastTickets.setOnClickListener(v -> {

                ticketAdapter.setFilter(EventFilterDateType.PAST);
                pastTickets.setBackgroundTintList(
                        ColorStateList.valueOf(
                                ContextCompat.getColor(this, R.color.action)
                        )
                );

                upcomingTickets.setBackgroundTintList(
                        ColorStateList.valueOf(
                                ContextCompat.getColor(this, R.color.black)
                        )
                );

                updateEmptyState();
            });

            upcomingTickets.setOnClickListener(v -> {

                ticketAdapter.setFilter(EventFilterDateType.UPCOMING);
                pastTickets.setBackgroundTintList(
                        ColorStateList.valueOf(
                                ContextCompat.getColor(this, R.color.black)
                        )
                );

                upcomingTickets.setBackgroundTintList(
                        ColorStateList.valueOf(
                                ContextCompat.getColor(this, R.color.action)
                        )
                );

                updateEmptyState();
            });
        }
    }

    private void fetchTickets(String userKey) {
        userTicketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ticketList.clear();

                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String eventID = ticketSnapshot.child("eventID").getValue(String.class);
                    String userID = ticketSnapshot.child("userID").getValue(String.class);

                    if (!userKey.equals(userID)) continue;

                    if (eventID != null) {

                        TicketItem ticket = new TicketItem(
                                ticketSnapshot.getKey(),
                                "",
                                0,
                                ""
                        );

                        ticket.setEventID(eventID);
                        ticketList.add(ticket);
                    }
                }

                enrichTicketsFromEventDatabase();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MyTicketsActivity.this, "Failed to load tickets", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enrichTicketsFromEventDatabase() {
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Map<String, EventItem> eventsById = new HashMap<>();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    EventItem event = eventSnapshot.getValue(EventItem.class);
                    if (event != null) {
                        event.setEventID(eventSnapshot.getKey());
                        eventsById.put(event.getEventID(), event);
                    }
                }

                for (TicketItem ticket : ticketList) {
                    EventItem event = eventsById.get(ticket.getEventID());
                    if (event != null) {
                        ticket.setTitle(event.getTitle());
                        ticket.setDateTime(event.getDateTime());
                        ticket.setLocation(event.getLocation());
                        ticket.setDetails(event.getDetails());
                        ticket.setImgURL(event.getImgURL());
                        ticket.setCategory(event.getCategory());
                        ticket.setCapacity(event.getCapacity());
                        ticket.setAttendeeCount(event.getAttendeeCount());
                    }
                }

                ticketAdapter.updateList(ticketList);
                updateEmptyState();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                ticketAdapter.updateList(ticketList);
                updateEmptyState();
            }
        });
    }

    private void cancelReservation(TicketItem ticket) {
        if (ticket == null || ticket.getTicketId() == null || ticket.getTicketId().trim().isEmpty()) {
            Toast.makeText(this, "Unable to cancel this ticket", Toast.LENGTH_SHORT).show();
            return;
        }


        TicketItem.cancelTicket(ticket.getEventID(), ticket.getTicketId(), new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(MyTicketsActivity.this, message, Toast.LENGTH_SHORT).show();
                sendNotification(UserInSession.getInstance().getUser(), ticket.getTitle(), NotificationType.CANCEL_TICKET, "");
            }

            @Override
            public void onError(String message) {
                Toast.makeText(MyTicketsActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openTicketDetails(TicketItem ticket) {
        if (ticket == null) {
            return;
        }

        Intent intent = new Intent(MyTicketsActivity.this, EventDetailActivity.class);
        intent.putExtra("EventID", ticket.getEventID());
        intent.putExtra("title", ticket.getTitle());
        intent.putExtra("dateTime", formatTime(ticket.getDateTime()));
        intent.putExtra("location", ticket.getLocation());
        intent.putExtra("details", ticket.getDetails());
        intent.putExtra("imgURL", ticket.getImgURL());
        intent.putExtra("eventCategory", ticket.getCategory().toString());
        intent.putExtra("capacity", ticket.getCapacity());
        intent.putExtra("attendees", ticket.getAttendeeCount());

        startActivity(intent);
    }

    private void updateEmptyState() {
        if (ticketAdapter.getItemCount() == 0) {
            emptyStateText.setVisibility(View.VISIBLE);
        } else {
            emptyStateText.setVisibility(View.GONE);
        }
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

