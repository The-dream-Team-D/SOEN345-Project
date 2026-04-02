package com.example.popin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private static final String NOT_PROVIDED = "Not provided";

    private TextView tvName, tvEmail, tvAddress, tvPhone, tvBio, tvRole;
    private EditText etName, etAddress, etPhone, etBio;
    private Button btnEdit, btnSave;
    private LinearLayout displayContainer, editContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        View navBar = findViewById(R.id.bottomNav);
        boolean isAdmin = UserInSession.getInstance().getUser().getIsAdmin();
        NavBarComponentView.setup(navBar, isAdmin);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvBio = findViewById(R.id.tvBio);
        tvRole = findViewById(R.id.tvRole);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhone = findViewById(R.id.etPhone);
        etBio = findViewById(R.id.etBio);

        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        displayContainer = findViewById(R.id.displayContainer);
        editContainer = findViewById(R.id.editContainer);

        loadUserData();

        btnEdit.setOnClickListener(v -> enableEditMode());
        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        User user = UserInSession.getInstance().getUser();

        String name = safeValue(user.getName());
        String email = safeValue(user.getEmail());
        String address = safeValue(user.getAddress());
        String phone = safeValue(user.getPhone());
        String bio = safeValue(user.getBio());
        String role = user.getIsAdmin() ? "Admin" : "User";

        tvName.setText("Name: " + name);
        tvEmail.setText("Email: " + email);
        tvAddress.setText("Address: " + address);
        tvPhone.setText("Phone: " + phone);
        tvBio.setText("Bio: " + bio);
        tvRole.setText("Account Type: " + role);

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
    }

    private void disableEditMode() {
        displayContainer.setVisibility(View.VISIBLE);
        editContainer.setVisibility(View.GONE);

        btnEdit.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
    }

    private void saveUserData() {
        String newName = etName.getText().toString().trim();
        String newAddress = etAddress.getText().toString().trim();
        String newPhone = etPhone.getText().toString().trim();
        String newBio = etBio.getText().toString().trim();

        if (newName.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = UserInSession.getInstance().getUser();
        user.setName(newName);
        user.setAddress(newAddress);
        user.setPhone(newPhone);
        user.setBio(newBio);

        user.updateProfile(new User.UpdateCallback() {
            @Override
            public void onSuccess() {
                loadUserData();
                disableEditMode();
                Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}