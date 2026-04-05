package com.example.popin.UIpages;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.popin.R;
import com.example.popin.logic.GenericCallback;
import com.example.popin.logic.NotificationPreferenceOptions;
import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;
import com.example.popin.reusableui.NavBarComponentView;
import com.google.android.material.card.MaterialCardView;

public class ProfileActivity extends AppCompatActivity {

    private static final String NOT_PROVIDED = "Not provided";


    private TextView tvName;
    private TextView tvEmail;
    private TextView tvAddress;
    private TextView tvPhone;
    private TextView tvBio;
    private TextView tvNotifPref;
    private EditText etName;
    private EditText etAddress;
    private EditText etPhone;
    private EditText etBio;
    private Button btnEdit;
    private Button btnSave;
    private Button btnLogout;
    private Button btnDelete;
    private LinearLayout displayContainer;
    private LinearLayout editContainer;

    private MaterialCardView smsOption;
    private MaterialCardView emailOption;

    private final boolean[] notificationPreference = {false, false};   // (email, SMS)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        View navBar = findViewById(R.id.bottomNav);
        NavBarComponentView.setup(navBar);


        ImageView questionMarks = findViewById(R.id.question_marks);
        Button loginButton = findViewById(R.id.LoginToViewProfileOptions);
        ScrollView profileScroll = findViewById(R.id.profileScroll);

        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {

            questionMarks.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            profileScroll.setVisibility(View.GONE);

            loginButton.setOnClickListener(v ->
                    startActivity(new Intent(this, LogInActivity.class)));


        } else {

            questionMarks.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            profileScroll.setVisibility(View.VISIBLE);

            tvName = findViewById(R.id.tvName);
            tvEmail = findViewById(R.id.tvEmail);
            tvAddress = findViewById(R.id.tvAddress);
            tvPhone = findViewById(R.id.tvPhone);
            tvBio = findViewById(R.id.tvBio);
            tvNotifPref = findViewById(R.id.tvNotifPref);

            etName = findViewById(R.id.etName);
            etAddress = findViewById(R.id.etAddress);
            etPhone = findViewById(R.id.etPhone);
            etBio = findViewById(R.id.etBio);

            btnEdit = findViewById(R.id.btnEdit);
            btnSave = findViewById(R.id.btnSave);
            btnLogout = findViewById(R.id.btnLogout);
            btnDelete = findViewById(R.id.btnDelete);


            displayContainer = findViewById(R.id.displayContainer);
            editContainer = findViewById(R.id.editContainer);

            loadUserData();

            btnEdit.setOnClickListener(v -> enableEditMode());
            btnSave.setOnClickListener(v -> saveUserData());

            btnLogout.setOnClickListener(v -> {

                UserInSession.clear();
                startActivity(new Intent(this, MainActivity.class));
                finish();

            });

            final int[] clickCount = {0};

            btnDelete.setOnClickListener(v -> {
                clickCount[0]++;
                if (clickCount[0] == 1) {
                    btnDelete.setText(R.string.warningText);
                    btnDelete.setTextColor(ContextCompat.getColorStateList(this, R.color.white));
                    btnDelete.setBackgroundTintList(
                            ColorStateList.valueOf(
                                    ContextCompat.getColor(this, R.color.warning)
                            )
                    );
                    btnDelete.postDelayed(() -> {
                        clickCount[0] = 0;
                        btnDelete.setText(R.string.deleteAccount);
                        btnDelete.setBackgroundTintList(
                                ColorStateList.valueOf(
                                        ContextCompat.getColor(this, R.color.action)
                                )
                        );
                    }, 3000);

                } else {
                    clickCount[0] = 0;
                    btnDelete.setText(R.string.deleteAccount);
                    btnDelete.setBackgroundTintList(
                            ColorStateList.valueOf(
                                    ContextCompat.getColor(this, R.color.action)
                            )
                    );

                    session.getUser().delete(new GenericCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            UserInSession.clear();
                            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


            emailOption = findViewById(R.id.boxEmail);
            smsOption = findViewById(R.id.boxSMS);

            if (session.getUser().getUserNotificationPreference() == NotificationPreferenceOptions.Email) {
                notificationPreference[0] = true;
                emailOption.setStrokeColor(ContextCompat.getColor(this,
                        R.color.action));
            } else {
                notificationPreference[1] = true;
                smsOption.setStrokeColor(ContextCompat.getColor(this,
                        R.color.action));
            }

            emailOption.setOnClickListener(v -> {
                notificationPreference[0] = true;
                notificationPreference[1] = false;

                emailOption.setStrokeColor(ContextCompat.getColor(this,
                        R.color.action));

                smsOption.setStrokeColor(ContextCompat.getColor(this,
                        R.color.black));
            });

            smsOption.setOnClickListener(v -> {
                notificationPreference[1] = true;
                notificationPreference[0] = false;

                smsOption.setStrokeColor(ContextCompat.getColor(this,
                        R.color.action));

                emailOption.setStrokeColor(ContextCompat.getColor(this,
                        R.color.black));
            });
        }
    }



    private void loadUserData() {
        User user = UserInSession.getInstance().getUser();

        String name = safeValue(user.getName());
        String email = safeValue(user.getEmail());
        String address = safeValue(user.getAddress());
        String phone = safeValue(user.getPhoneNumber());
        String bio = safeValue(user.getBio());
        String notifPref = safeValue(user.getUserNotificationPreference().toString());

        tvName.setText("Name: " + name);
        tvEmail.setText("Email: " + email);
        tvAddress.setText("Address: " + address);
        tvPhone.setText("Phone: " + phone);
        tvBio.setText("Bio: " + bio);
        tvNotifPref.setText("Notification Preference:" + notifPref);



        etName.setText(name.equals(NOT_PROVIDED) ? "" : name);
        etAddress.setText(address.equals(NOT_PROVIDED) ? "" : address);
        etPhone.setText(phone.equals(NOT_PROVIDED) ? "" : phone);
        etBio.setText(bio.equals(NOT_PROVIDED) ? "" : bio);
    }

    private String safeValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return NOT_PROVIDED;
        }
        return value;
    }

    private void enableEditMode() {
        displayContainer.setVisibility(View.GONE);
        editContainer.setVisibility(View.VISIBLE);

        btnEdit.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.GONE);
    }

    private void disableEditMode() {
        displayContainer.setVisibility(View.VISIBLE);
        editContainer.setVisibility(View.GONE);

        btnEdit.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        btnLogout.setVisibility(View.VISIBLE);

    }

    private void saveUserData() {
        String newName = etName.getText().toString().trim();
        String newAddress = etAddress.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();
        String newBio = etBio.getText().toString().trim();
        NotificationPreferenceOptions newNotificationPreference;

        if (notificationPreference[0]) {
            newNotificationPreference = NotificationPreferenceOptions.Email;
        } else{
            newNotificationPreference = NotificationPreferenceOptions.SMS;
        }

        if (newName.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = UserInSession.getInstance().getUser();
        user.setName(newName);
        user.setAddress(newAddress);
        user.setPhoneNumber(newPhone);
        user.setBio(newBio);
        String notifChangeResult = user.setUserNotificationPreference(newNotificationPreference);

        if (!notifChangeResult.equals("Preference updated successfully")) {
            Toast.makeText(this, notifChangeResult, Toast.LENGTH_SHORT).show();
            return;
        }

        user.updateProfile(new GenericCallback() {
            @Override
            public void onSuccess(String message) {
                loadUserData();
                disableEditMode();
                Toast.makeText(ProfileActivity.this,  message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

