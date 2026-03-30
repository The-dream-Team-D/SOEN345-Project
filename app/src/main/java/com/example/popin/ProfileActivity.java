package com.example.popin;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        View navBar = findViewById(R.id.bottomNav);
        User sessionUser = UserInSession.getCurrentUser();
        boolean userInSessionAdminCheck = sessionUser != null && sessionUser.getIsAdmin();
        NavBarComponentView.setup(navBar, userInSessionAdminCheck);
    }
}
