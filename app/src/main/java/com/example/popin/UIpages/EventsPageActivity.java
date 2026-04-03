package com.example.popin.UIpages;

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

import com.example.popin.R;
import com.example.popin.logic.EventAdapter;
import com.example.popin.logic.EventCategory;
import com.example.popin.logic.EventItem;
import com.example.popin.logic.UserInSession;
import com.example.popin.reusableUI.NavBarComponentView;
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

        View navBar = findViewById(R.id.bottomNav);

        NavBarComponentView.setup(navBar);


        eventList = new ArrayList<>();
        
        RecyclerView recyclerView = findViewById(R.id.rvEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Event database");

        // Check if DB is empty and upload sample data if needed
        checkAndUploadSampleData();

        // Fetch events from Firebase
        fetchEvents();

        EditText searchInput = findViewById(R.id.etSearchEvents);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                eventAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void checkAndUploadSampleData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Database is empty, upload the "shit" you wanted
                    List<EventItem> sampleEvents = Arrays.asList(
                            new EventItem(
                                    "SOEN Mixer",
                                    EventItem.convertTimeToLong(2026, 2, 20, 18, 0),
                                    "EV Building Lobby",
                                    "Meet other SOEN students, network, and enjoy snacks in a casual social setting.",
                                    "https://images.stockcake.com/public/9/6/d/96d4100c-ca71-4e09-b84e-d7e90c294a87_large/joyful-party-celebration-stockcake.jpg",
                                    100,
                                    EventCategory.Social
                            ),
                            new EventItem(
                                    "Study Time and Project Submission",
                                    EventItem.convertTimeToLong(2026, 3, 20, 18, 0),
                                    "H Building 805",
                                    "Submit and finish all your work.",
                                    "https://images.stockcake.com/public/9/6/d/96d4100c-ca71-4e09-b84e-d7e90c294a87_large/joyful-party-celebration-stockcake.jpg",
                                    1,
                                    EventCategory.Educational
                            ),
                            new EventItem(
                                    "Board Games Night",
                                    EventItem.convertTimeToLong(2026, 2, 22, 19, 30),
                                    "Hall A",
                                    "Join us for an evening of board games, team challenges, and friendly competition.",
                                    "https://cdn.apartmenttherapy.info/image/upload/v1667575155/stock/custom%20stock/2022-11-custom-stock/games-0228-edit.jpg",
                                    50,
                                    EventCategory.Entertainment
                            ),
                            new EventItem(
                                    "Hackathon Kickoff",
                                    EventItem.convertTimeToLong(2026, 2, 24, 17, 0),
                                    "Room H-937",
                                    "Kick off the semester hackathon with team formation, project ideas, and event rules.",
                                    "https://ezassi.com/wp-content/uploads/2024/10/hackathon.png",
                                    200,
                                    EventCategory.Professional
                            ),
                            new EventItem(
                                    "Coffee and Code",
                                    EventItem.convertTimeToLong(2026, 2, 26, 14, 0),
                                    "Library Cafe",
                                    "Bring your laptop, grab a coffee, and code with classmates in a relaxed environment.",
                                    "https://localist-images.azureedge.net/photos/52499165824998/card/2d55307e23bf99b05af70bcb92b61f94607cdb85.jpg",
                                    40,
                                    EventCategory.Educational
                            ),
                            new EventItem(
                                    "AI Study Jam",
                                    EventItem.convertTimeToLong(2026, 2, 28, 16, 30),
                                    "Engineering Lounge",
                                    "Review AI concepts, solve practice problems, and prepare together for upcoming exams.",
                                    "https://res.cloudinary.com/startup-grind/image/upload/c_fill,dpr_2.0,f_auto,g_center,q_auto:good/v1/gcs/platform-data-goog/events/On%20Campus%20%283%29_026JzWM.png",
                                    60,
                                    EventCategory.Educational
                            )
                    );

                    for (EventItem event : sampleEvents) {
                        databaseReference.push().setValue(event);
                    }
                    Toast.makeText(EventsPageActivity.this, "Sample events uploaded!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EventsPageActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
