package com.example.popin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventsPageActivity extends AppCompatActivity {
    private EventAdapter eventAdapter;
    private List<EventItem> eventList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);

        setupNavBar();
        setupRecyclerView();
        setupFirebase();
        setupSearch();
    }

    private void setupNavBar() {
        View navBar = findViewById(R.id.bottomNav);

        if (navBar == null) {
            Log.e("EventsPageActivity", "bottomNav view not found");
            return;
        }

        User currentUser = UserInSession.getInstance().getUser();
        boolean userInSessionAdminCheck = currentUser != null && currentUser.getIsAdmin();

        Log.d("EventsPageActivity", "Current user: "
                + (currentUser != null ? currentUser.getEmail() : "null"));
        Log.d("EventsPageActivity", "Is admin: " + userInSessionAdminCheck);

        NavBarComponentView.setup(navBar, userInSessionAdminCheck);
    }

    private void setupRecyclerView() {
        eventList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rvEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);
    }

    private void setupFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Event database");

        checkAndUploadSampleData();
        fetchEvents();
    }

    private void setupSearch() {
        EditText searchInput = findViewById(R.id.etSearchEvents);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkAndUploadSampleData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    List<EventItem> sampleEvents = Arrays.asList(
                            new EventItem(
                                    "SOEN Mixer",
                                    "March 20, 2026 - 6:00 PM",
                                    "EV Building Lobby",
                                    "Meet other SOEN students, network, and enjoy snacks in a casual social setting."
                            ),
                            new EventItem(
                                    "Board Games Night",
                                    "March 22, 2026 - 7:30 PM",
                                    "Hall A",
                                    "Join us for an evening of board games, team challenges, and friendly competition."
                            ),
                            new EventItem(
                                    "Hackathon Kickoff",
                                    "March 24, 2026 - 5:00 PM",
                                    "Room H-937",
                                    "Kick off the semester hackathon with team formation, project ideas, and event rules."
                            ),
                            new EventItem(
                                    "Coffee and Code",
                                    "March 26, 2026 - 2:00 PM",
                                    "Library Cafe",
                                    "Bring your laptop, grab a coffee, and code with classmates in a relaxed environment."
                            ),
                            new EventItem(
                                    "AI Study Jam",
                                    "March 28, 2026 - 4:30 PM",
                                    "Engineering Lounge",
                                    "Review AI concepts, solve practice problems, and prepare together for upcoming exams."
                            )
                    );

                    for (EventItem event : sampleEvents) {
                        databaseReference.push().setValue(event);
                    }

                    Toast.makeText(
                            EventsPageActivity.this,
                            "Sample events uploaded!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EventsPageActivity", "Error checking DB: " + error.getMessage());
            }
        });
    }

    private void fetchEvents() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EventItem event = dataSnapshot.getValue(EventItem.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }

                eventAdapter.updateList(eventList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EventsPageActivity", "Database error: " + error.getMessage());
                Toast.makeText(
                        EventsPageActivity.this,
                        "Failed to load events",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}