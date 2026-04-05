package com.example.popin.uipages;

import static com.example.popin.logic.EventItem.convertTimeToLong;
import static java.lang.Integer.parseInt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.popin.logic.EventAdapter;
import com.example.popin.logic.EventItem;
import com.example.popin.reusableui.EventsFilterComponentView;
import com.example.popin.reusableui.NavBarComponentView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popin.R;
import com.example.popin.addedfiles.Admin;
import com.example.popin.addedfiles.EventCatalog;
import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private LinearLayout adminDashboardTabs;

    private ScrollView sectionAddEvent;
    private ScrollView sectionUpdateEvent;
    private ScrollView sectionDeleteEvent;
    private EditText addTitleInput;
    private EditText addDateTimeInput;
    private EditText addLocationInput;
    private EditText addDescriptionInput;

    private EditText currentUpdateTitleInput;

    private EditText updateTitleInput;
    private EditText updateDateInput;
    private EditText updateLocationInput;
    private EditText updateDescriptionInput;
    private EditText updateCapacityInput;



    private EditText deleteEventNameInput;

    private Button addEventButton;
    private Button updateEventButton;
    private Button deleteEventButton;
    private Button goToAddEventButton;
    private Button goToUpdateEventButton;
    private Button goToDeleteEventButton;
    private Button backButton;

    private Admin adminUser;

    private EventAdapter eventAdapter;
    private List<EventItem> eventList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        View navBar = findViewById(R.id.bottomNav);
        if (navBar == null) {
            Toast.makeText(this, "Navbar not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        User currentUser = UserInSession.getInstance().getUser();
        if (currentUser == null) {
            Toast.makeText(this, "No user in session", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        NavBarComponentView.setup(navBar);

        if (!(currentUser instanceof Admin)) {
            Toast.makeText(this, "Current user is not an admin", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        eventList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rvEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);


        adminUser = (Admin) currentUser;

        bindViews();
        setupListeners();

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("Event database");

        // Fetch events from Firebase
        fetchEvents();



    }

    private void bindViews() {

        adminDashboardTabs = findViewById(R.id.adminDashboardTabs);

        sectionAddEvent = findViewById(R.id.sectionAddEvent);
        sectionUpdateEvent = findViewById(R.id.sectionUpdateEvent);
        sectionDeleteEvent = findViewById(R.id.sectionDeleteEvent);

        goToAddEventButton = findViewById(R.id.btnCreateEvent);
        goToUpdateEventButton = findViewById(R.id.btnUpdateEvent);
        goToDeleteEventButton = findViewById(R.id.btnDeleteEvent);
        backButton = findViewById(R.id.btnBack);

        addTitleInput = findViewById(R.id.editText4);
        addDateTimeInput = findViewById(R.id.addDateTime);
        addLocationInput = findViewById(R.id.editText2);
        addDescriptionInput = findViewById(R.id.editText3);

        currentUpdateTitleInput = findViewById(R.id.editTextCurrentEventName);
        updateTitleInput = findViewById(R.id.editText9);
        updateDateInput = findViewById(R.id.editDateTime);
        updateLocationInput = findViewById(R.id.editText10);
        updateDescriptionInput = findViewById(R.id.editText11);
        updateCapacityInput = findViewById(R.id.newCapacityInput);

        deleteEventNameInput = findViewById(R.id.textInputEditText);

        addEventButton = findViewById(R.id.addEvent);
        updateEventButton = findViewById(R.id.button3);
        deleteEventButton = findViewById(R.id.button2);

    }

    private void setupListeners() {


        goToAddEventButton.setOnClickListener(v -> {
            adminDashboardTabs.setVisibility(View.GONE);
            sectionAddEvent.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        });
        goToUpdateEventButton.setOnClickListener(v -> {
            adminDashboardTabs.setVisibility(View.GONE);
            sectionUpdateEvent.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        });
        goToDeleteEventButton.setOnClickListener(v -> {
            adminDashboardTabs.setVisibility(View.GONE);
            sectionDeleteEvent.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
        });


        backButton.setOnClickListener(v -> {

            sectionDeleteEvent.setVisibility(View.GONE);
            sectionUpdateEvent.setVisibility(View.GONE);
            sectionAddEvent.setVisibility(View.GONE);
            backButton.setVisibility(View.GONE);

            adminDashboardTabs.setVisibility(View.VISIBLE);
        });

        addEventButton.setOnClickListener(v -> addEvent());
        updateEventButton.setOnClickListener(v -> updateEvent());
        deleteEventButton.setOnClickListener(v -> deleteEvent());


        EventsFilterComponentView filterView = findViewById(R.id.commonEventsFilter);

        filterView.setOnFilterChangeListener((
                query,
                categories,
                next30Days) -> {
            eventAdapter.filter(query, categories, next30Days, false);
        });

    }

    private void addEvent() {
        String title = getText(addTitleInput);
        String dateText = getText(addDateTimeInput);
        String location = getText(addLocationInput);
        String description = getText(addDescriptionInput);

        if (title.isEmpty() || dateText.isEmpty()
                || location.isEmpty() || description.isEmpty()) {
            showToast("Please fill all Add Event fields");
            return;
        }

        long eventDate = processDateTime(dateText);
        if(eventDate == -1){return;}


        adminUser.addEvent(
                title,
                location,
                description,
                eventDate,
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        showToast(message);
                        clearAddFields();

                        sectionAddEvent.setVisibility(View.GONE);
                        backButton.setVisibility(View.GONE);
                        adminDashboardTabs.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(String message) {
                        showToast(message);
                    }
                }
        );
    }

    private void updateEvent() {
        String currentEventName = getText(currentUpdateTitleInput);
        String newName = getText(updateTitleInput);
        String dateText = getText(updateDateInput);
        String location = getText(updateLocationInput);
        String description = getText(updateDescriptionInput);
        String capacity = getText(updateCapacityInput);

        if (currentEventName.isEmpty()) {
            showToast("Enter the event name to update");
            return;
        }

        long newDate = -1;
        if (!dateText.isEmpty()) {
            newDate = processDateTime(dateText);
            if(newDate == -1){return;}
        }

        String newLocation = location.isEmpty() ? null : location;
        String newDescription = description.isEmpty() ? null : description;
        int newCapacity;

        try {
            newCapacity = parseInt(capacity);

            if(newCapacity <= 0){
                showToast("New Event Capacity can not be 0 or less");
                return;
            }
        } catch (Exception e) {
            newCapacity = -1;
        }
        adminUser.updateEvent(
                currentEventName,
                newName,
                newLocation,
                newDescription,
                newDate,
                null,
                newCapacity,
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        showToast(message);
                        clearUpdateFields();


                        sectionUpdateEvent.setVisibility(View.GONE);
                        backButton.setVisibility(View.GONE);
                        adminDashboardTabs.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(String message) {
                        showToast(message);
                    }
                }
        );
    }

    private void deleteEvent() {
        String eventName = getText(deleteEventNameInput);

        if (eventName.isEmpty()) {
            showToast("Enter the event name to delete");
            return;
        }

        adminUser.removeEvent(eventName, new EventCatalog.EventActionCallback() {
            @Override
            public void onSuccess(String message) {
                showToast(message);
                deleteEventNameInput.setText("");

                sectionDeleteEvent.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
                adminDashboardTabs.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                showToast(message);
            }
        });
    }

    private String getText(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }

    private void clearAddFields() {
        addTitleInput.setText("");
        addDateTimeInput.setText("");
        addLocationInput.setText("");
        addDescriptionInput.setText("");

    }

    private void clearUpdateFields() {
        updateTitleInput.setText("");
        updateDateInput.setText("");
        updateLocationInput.setText("");
        updateDescriptionInput.setText("");
        updateCapacityInput.setText("");

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
                eventAdapter.updateList(eventList, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("EventsPageActivity", "Database error: " + error.getMessage());
                Toast.makeText(AdminDashboardActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public long processDateTime(String dateText){
        String[] parts = dateText.split("-");

        if (parts.length < 5) {
            showToast("Incomplete date format. Please use YYYY-MM-DD-HH-mm");
            return -1 ;
        }

        try {
            int year   = Integer.parseInt(parts[0]);
            int month  = Integer.parseInt(parts[1]) - 1;
            int day    = Integer.parseInt(parts[2]);
            int hour   = Integer.parseInt(parts[3]);
            int minute = Integer.parseInt(parts[4]);

            if (month < 0 || month > 11) { showToast("Invalid Month (1-12)"); return -1; }
            if (day < 1 || day > 31) { showToast("Invalid Day"); return -1; }
            if (hour < 0 || hour > 23) { showToast("Invalid Hour (0-23)"); return -1; }
            if (minute < 0 || minute > 59) { showToast("Invalid Minute (0-59)"); return -1; }

            long newDate = convertTimeToLong(year, month, day, hour, minute);
            long now = System.currentTimeMillis();

            if(now > newDate){
                showToast("Cannot Create Event in the past");
                return -1 ;
            }

            return newDate;
        } catch (NumberFormatException e) {
            showToast("Invalid characters. Use numbers and hyphens only.");
            return -1;
        }
    }
}


