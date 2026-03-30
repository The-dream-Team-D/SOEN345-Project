package com.example.popin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminDashboardActivity extends AppCompatActivity {

    private EditText addTitleInput;
    private EditText addDateInput;
    private EditText addTimeInput;
    private EditText addLocationInput;
    private EditText addDescriptionInput;

    private EditText updateTitleInput;
    private EditText updateDateInput;
    private EditText updateTimeInput;
    private EditText updateLocationInput;
    private EditText updateDescriptionInput;

    private EditText deleteEventNameInput;

    private Button addEventButton;
    private Button updateEventButton;
    private Button deleteEventButton;

    private Admin adminUser;

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

        User currentUser = UserInSession.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "No user in session", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        NavBarComponentView.setup(navBar, currentUser.getIsAdmin());

        if (!(currentUser instanceof Admin)) {
            Toast.makeText(this, "Current user is not an admin", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adminUser = (Admin) currentUser;

        bindViews();
        setupListeners();
    }

    private void bindViews() {
        addTitleInput = findViewById(R.id.editText4);
        addDateInput = findViewById(R.id.editTextDate);
        addTimeInput = findViewById(R.id.editTextTime);
        addLocationInput = findViewById(R.id.editText2);
        addDescriptionInput = findViewById(R.id.editText3);

        updateTitleInput = findViewById(R.id.editText9);
        updateDateInput = findViewById(R.id.editTextDate3);
        updateTimeInput = findViewById(R.id.editTextTime3);
        updateLocationInput = findViewById(R.id.editText10);
        updateDescriptionInput = findViewById(R.id.editText11);

        deleteEventNameInput = findViewById(R.id.textInputEditText);

        addEventButton = findViewById(R.id.addEvent);
        updateEventButton = findViewById(R.id.button3);
        deleteEventButton = findViewById(R.id.button2);
    }

    private void setupListeners() {
        addEventButton.setOnClickListener(v -> addEvent());
        updateEventButton.setOnClickListener(v -> updateEvent());
        deleteEventButton.setOnClickListener(v -> deleteEvent());
    }

    private void addEvent() {
        String title = getText(addTitleInput);
        String dateText = getText(addDateInput);
        String timeText = getText(addTimeInput);
        String location = getText(addLocationInput);
        String description = getText(addDescriptionInput);

        if (title.isEmpty() || dateText.isEmpty() || timeText.isEmpty()
                || location.isEmpty() || description.isEmpty()) {
            showToast("Please fill all Add Event fields");
            return;
        }

        Date eventDate = parseDateTime(dateText, timeText);
        if (eventDate == null) {
            showToast("Invalid date/time format. Use yyyy-MM-dd and HH:mm");
            return;
        }

        adminUser.addEvent(
                title,
                location,
                description,
                eventDate,
                EventCategory.CONCERT,
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        showToast(message);
                        clearAddFields();
                    }

                    @Override
                    public void onError(String message) {
                        showToast(message);
                    }
                }
        );
    }

    private void updateEvent() {
        String currentEventName = getText(updateTitleInput);
        String dateText = getText(updateDateInput);
        String timeText = getText(updateTimeInput);
        String location = getText(updateLocationInput);
        String description = getText(updateDescriptionInput);

        if (currentEventName.isEmpty()) {
            showToast("Enter the event name to update");
            return;
        }

        Date newDate = null;
        if (!dateText.isEmpty() && !timeText.isEmpty()) {
            newDate = parseDateTime(dateText, timeText);
            if (newDate == null) {
                showToast("Invalid date/time format. Use yyyy-MM-dd and HH:mm");
                return;
            }
        }

        String newName = currentEventName;
        String newLocation = location.isEmpty() ? null : location;
        String newDescription = description.isEmpty() ? null : description;

        adminUser.updateEvent(
                currentEventName,
                newName,
                newLocation,
                newDescription,
                newDate,
                null,
                null,
                new EventCatalog.EventActionCallback() {
                    @Override
                    public void onSuccess(String message) {
                        showToast(message);
                        clearUpdateFields();
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

    private Date parseDateTime(String dateText, String timeText) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        formatter.setLenient(false);

        try {
            return formatter.parse(dateText + " " + timeText);
        } catch (ParseException e) {
            return null;
        }
    }

    private void clearAddFields() {
        addTitleInput.setText("");
        addDateInput.setText("");
        addTimeInput.setText("");
        addLocationInput.setText("");
        addDescriptionInput.setText("");
    }

    private void clearUpdateFields() {
        updateTitleInput.setText("");
        updateDateInput.setText("");
        updateTimeInput.setText("");
        updateLocationInput.setText("");
        updateDescriptionInput.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}