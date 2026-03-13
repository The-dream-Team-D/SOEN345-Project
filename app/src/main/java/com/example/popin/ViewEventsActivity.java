package com.example.popin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class ViewEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_events);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.viewEventsRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        List<EventItem> events = Arrays.asList(
                new EventItem("SOEN Mixer", "March 20, 2026 - 6:00 PM", "EV Building Lobby"),
                new EventItem("Board Games Night", "March 22, 2026 - 7:30 PM", "Hall A"),
                new EventItem("Hackathon Kickoff", "March 24, 2026 - 5:00 PM", "Room H-937"),
                new EventItem("Coffee and Code", "March 26, 2026 - 2:00 PM", "Library Cafe"),
                new EventItem("AI Study Jam", "March 28, 2026 - 4:30 PM", "Engineering Lounge")
        );

        RecyclerView recyclerView = findViewById(R.id.rvEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        EventAdapter eventAdapter = new EventAdapter(events);
        recyclerView.setAdapter(eventAdapter);

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
}
