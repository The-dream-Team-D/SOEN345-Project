package com.example.popin.uipages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import com.example.popin.reusableui.EventsFilterComponentView;
import com.example.popin.reusableui.NavBarComponentView;
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
    private TextView emptyStateText;


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

        emptyStateText = findViewById(R.id.tvEventsEmptyState);


        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Event database");

        // Fetch events from Firebase
        fetchEvents();

        EventsFilterComponentView filterView = findViewById(R.id.commonEventsFilter);

        filterView.setOnFilterChangeListener((
                query,
                categories,
                next30Days) -> {
            eventAdapter.filter(query, categories, next30Days, true);
            updateEmptyState();
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
                eventAdapter.updateList(eventList, true);
                updateEmptyState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EventsPageActivity", "Database error: " + error.getMessage());
                Toast.makeText(EventsPageActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
                updateEmptyState();
            }
        });
    }



    private void updateEmptyState() {
        if (eventAdapter.getItemCount() == 0) {
            emptyStateText.setVisibility(View.VISIBLE);
        } else {
            emptyStateText.setVisibility(View.GONE);
        }
    }


}

