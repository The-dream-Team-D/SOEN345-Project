package com.example.popin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

    private Spinner spinnerDate;
    private Spinner spinnerLocation;
    private Spinner spinnerCategory;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);

        View navBar = findViewById(R.id.bottomNav);
        boolean userInSessionAdminCheck = UserInSession.getInstance().getUser().getIsAdmin();
        NavBarComponentView.setup(navBar, userInSessionAdminCheck);

        eventList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rvEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        searchInput = findViewById(R.id.etSearchEvents);
        spinnerDate = findViewById(R.id.spinnerDate);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Date", "All Dates", "March 20, 2026", "March 22, 2026", "March 24, 2026", "March 26, 2026", "March 28, 2026")
        );
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(dateAdapter);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Location", "All Locations", "EV Building Lobby", "Hall A", "Room H-937", "Library Cafe", "Engineering Lounge")
        );
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Category", "All Categories", "Social", "Games", "Hackathon", "Coding", "Study")
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);


        spinnerDate.post(() -> {
            TextView tv = (TextView) spinnerDate.getChildAt(0);
            if (tv != null) tv.setGravity(android.view.Gravity.CENTER);
        });

        spinnerLocation.post(() -> {
            TextView tv = (TextView) spinnerLocation.getChildAt(0);
            if (tv != null) tv.setGravity(android.view.Gravity.CENTER);
        });

        spinnerCategory.post(() -> {
            TextView tv = (TextView) spinnerCategory.getChildAt(0);
            if (tv != null) tv.setGravity(android.view.Gravity.CENTER);
        });

        AdapterView.OnItemSelectedListener filterListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        spinnerDate.setOnItemSelectedListener(filterListener);
        spinnerLocation.setOnItemSelectedListener(filterListener);
        spinnerCategory.setOnItemSelectedListener(filterListener);

        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Event database");

        checkAndUploadSampleData();
        fetchEvents();

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void applyFilters() {
        String query = searchInput.getText().toString().trim();
        String selectedDate = spinnerDate.getSelectedItem().toString();
        String selectedLocation = spinnerLocation.getSelectedItem().toString();
        String selectedCategory = spinnerCategory.getSelectedItem().toString();

        eventAdapter.filter(query, selectedDate, selectedLocation, selectedCategory);
    }

    private void checkAndUploadSampleData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    List<EventItem> sampleEvents = Arrays.asList(
                            new EventItem("SOEN Mixer","March 20, 2026 - 6:00 PM","EV Building Lobby","Meet other SOEN students...","Social"),
                            new EventItem("Board Games Night","March 22, 2026 - 7:30 PM","Hall A","Join us for board games...","Games"),
                            new EventItem("Hackathon Kickoff","March 24, 2026 - 5:00 PM","Room H-937","Kick off the hackathon...","Hackathon"),
                            new EventItem("Coffee and Code","March 26, 2026 - 2:00 PM","Library Cafe","Code with classmates...","Coding"),
                            new EventItem("AI Study Jam","March 28, 2026 - 4:30 PM","Engineering Lounge","Review AI concepts...","Study")
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
                applyFilters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EventsPageActivity", "Database error: " + error.getMessage());
                Toast.makeText(EventsPageActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}