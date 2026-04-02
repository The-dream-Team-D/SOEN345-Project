package com.example.popin.UIpages;

import static com.example.popin.logic.Notifications.sendNotification;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popin.R;
import com.example.popin.logic.EventItem;
import com.example.popin.logic.GenericCallback;
import com.example.popin.logic.NotificationType;
import com.example.popin.logic.TicketAdapter;
import com.example.popin.logic.TicketItem;
import com.example.popin.logic.UserInSession;
import com.example.popin.reusableUI.NavBarComponentView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

        ImageView questionMarks = findViewById(R.id.question_marks);
        Button loginButton = findViewById(R.id.LoginToGetTickets);

        View navBar = findViewById(R.id.bottomNav);
        NavBarComponentView.setup(navBar);

        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            recyclerView.setVisibility(View.GONE);
            searchInput.setVisibility(View.GONE);

            questionMarks.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);

            loginButton.setOnClickListener(v ->
                    startActivity(new Intent(this, LogInActivity.class)));


        } else {

            recyclerView.setVisibility(View.VISIBLE);
            searchInput.setVisibility(View.VISIBLE);

            questionMarks.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);


            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            emptyStateText = findViewById(R.id.tvTicketsEmptyState);
            ticketAdapter = new TicketAdapter(ticketList, this::cancelReservation, this::openTicketDetails);
            recyclerView.setAdapter(ticketAdapter);

            String userKey = sanitizeKey(session.getUser().getEmail());
            userTicketsRef = FirebaseDatabase.getInstance()
                    .getReference("User tickets")
                    .child(userKey);
            eventsRef = FirebaseDatabase.getInstance().getReference("Event database");

            fetchTickets();

            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ticketAdapter.filter(s.toString());
                    updateEmptyState();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }

    private void fetchTickets() {
        userTicketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ticketList.clear();

                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    String title = ticketSnapshot.child("title").getValue(String.class);
                    if (title != null && !title.trim().isEmpty()) {
                        TicketItem ticket = new TicketItem(ticketSnapshot.getKey(), title, "", "");
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
                Map<String, EventItem> eventsByTitle = new HashMap<>();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    EventItem event = eventSnapshot.getValue(EventItem.class);
                    if (event != null && event.getTitle() != null) {
                        eventsByTitle.put(normalize(event.getTitle()), event);
                    }
                }

                for (TicketItem ticket : ticketList) {
                    EventItem event = eventsByTitle.get(normalize(ticket.getTitle()));
                    if (event != null) {
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

        String userKey = sanitizeKey(UserInSession.getInstance().getUser().getEmail());

        TicketItem.cancelTicket(userKey, ticket.getTitle(), ticket.getTicketId(), new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(MyTicketsActivity.this, message, Toast.LENGTH_SHORT).show();
                sendNotification(UserInSession.getInstance().getUser(), ticket.getTitle(), NotificationType.CancelTicket);
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
        intent.putExtra("title", ticket.getTitle());
        intent.putExtra("dateTime", ticket.getDateTime());
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

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
