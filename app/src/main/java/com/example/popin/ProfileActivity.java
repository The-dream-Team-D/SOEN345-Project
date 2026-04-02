package com.example.popin;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail;
    private EditText etName, etEmail;
    private Button btnEdit, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Navbar setup
        View navBar = findViewById(R.id.bottomNav);
        boolean isAdmin = UserInSession.getInstance().getUser().getIsAdmin();
        NavBarComponentView.setup(navBar, isAdmin);

        // UI references
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        loadUserData();

        btnEdit.setOnClickListener(v -> enableEditMode());
        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        User user = UserInSession.getInstance().getUser();

        tvName.setText("Name: " + user.getName());
        tvEmail.setText("Email: " + user.getEmail());

        etName.setText(user.getName());
        etEmail.setText(user.getEmail());
    }

    private void enableEditMode() {
        tvName.setVisibility(View.GONE);
        tvEmail.setVisibility(View.GONE);

        etName.setVisibility(View.VISIBLE);
        etEmail.setVisibility(View.VISIBLE);

        btnEdit.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
    }

    private void saveUserData() {
        String newName = etName.getText().toString();
        String newEmail = etEmail.getText().toString();

        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = UserInSession.getInstance().getUser();

        user.setName(newName);
        // ⚠️ Email NOT updated (safer for Firebase query)

        user.updateProfile(new User.UpdateCallback() {
            @Override
            public void onSuccess() {
                loadUserData();

                tvName.setVisibility(View.VISIBLE);
                tvEmail.setVisibility(View.VISIBLE);

                etName.setVisibility(View.GONE);
                etEmail.setVisibility(View.GONE);

                btnEdit.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.GONE);

                Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}