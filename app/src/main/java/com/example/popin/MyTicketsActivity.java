package com.example.popin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyTicketsActivity extends AppCompatActivity {
    private TicketAdapter ticketAdapter;
    private final List<TicketItem> ticketList = new ArrayList<>();
    private DatabaseReference userTicketsRef;
    private TextView emptyStateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tickets);

        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        View navBar = findViewById(R.id.bottomNav);
        boolean userInSessionAdminCheck = session.getUser().getIsAdmin();
        NavBarComponentView.setup(navBar, userInSessionAdminCheck);

        RecyclerView recyclerView = findViewById(R.id.rvTickets);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        emptyStateText = findViewById(R.id.tvTicketsEmptyState);
        ticketAdapter = new TicketAdapter(ticketList, this::cancelReservation);
        recyclerView.setAdapter(ticketAdapter);

        String userKey = sanitizeKey(session.getUser().getEmail());
        userTicketsRef = FirebaseDatabase.getInstance()
                .getReference("User tickets")
                .child(userKey);

        fetchTickets();

        EditText searchInput = findViewById(R.id.etSearchEvents);
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

    private void fetchTickets() {
        userTicketsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ticketList.clear();

                for (DataSnapshot ticketSnapshot : snapshot.getChildren()) {
                    TicketItem ticket = ticketSnapshot.getValue(TicketItem.class);
                    if (ticket != null) {
                        ticket.setTicketId(ticketSnapshot.getKey());
                        ticketList.add(ticket);
                    }
                }

                ticketAdapter.updateList(ticketList);
                updateEmptyState();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MyTicketsActivity.this, "Failed to load tickets", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelReservation(TicketItem ticket) {
        if (ticket == null || ticket.getTicketId() == null || ticket.getTicketId().trim().isEmpty()) {
            Toast.makeText(this, "Unable to cancel this ticket", Toast.LENGTH_SHORT).show();
            return;
        }

        userTicketsRef.child(ticket.getTicketId())
                .removeValue()
                .addOnSuccessListener(unused ->
                        Toast.makeText(MyTicketsActivity.this, "Ticket cancelled", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(unused ->
                        Toast.makeText(MyTicketsActivity.this, "Cancellation failed", Toast.LENGTH_SHORT).show());
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
