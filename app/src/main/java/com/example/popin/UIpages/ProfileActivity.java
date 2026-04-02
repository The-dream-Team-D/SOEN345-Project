package com.example.popin.UIpages;

import android.content.Intent;
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

import com.example.popin.R;
import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;
import com.example.popin.reusableUI.NavBarComponentView;

public class ProfileActivity extends AppCompatActivity {

    private static final String NOT_PROVIDED = "Not provided";


    private TextView tvName, tvEmail, tvAddress, tvPhone, tvBio, tvRole;
    private EditText etName, etAddress, etPhone, etBio;
    private Button btnEdit, btnSave, btnLogout;
    private LinearLayout displayContainer, editContainer;

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
            tvRole = findViewById(R.id.tvRole);

            etName = findViewById(R.id.etName);
            etAddress = findViewById(R.id.etAddress);
            etPhone = findViewById(R.id.etPhone);
            etBio = findViewById(R.id.etBio);

            btnEdit = findViewById(R.id.btnEdit);
            btnSave = findViewById(R.id.btnSave);

            displayContainer = findViewById(R.id.displayContainer);
            editContainer = findViewById(R.id.editContainer);
            btnLogout = findViewById(R.id.btnLogout);

            loadUserData();

            btnEdit.setOnClickListener(v -> enableEditMode());
            btnSave.setOnClickListener(v -> saveUserData());

            btnLogout.setOnClickListener(v ->{

                UserInSession.clear();
                startActivity(new Intent(this, MainActivity.class));
                finish();

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

        if (newName.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = UserInSession.getInstance().getUser();
        user.setName(newName);
        user.setAddress(newAddress);
        user.setPhoneNumber(newPhone);
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

    private void Logout(){

        UserInSession.clear();


    }
}
