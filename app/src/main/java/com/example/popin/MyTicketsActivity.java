package com.example.popin;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MyTicketsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tickets);

        View navBar = findViewById(R.id.bottomNav);
        boolean userInSessionAdminCheck = UserInSession.getInstance().getUser().getIsAdmin();
        NavBarComponentView.setup(navBar, userInSessionAdminCheck);

    }
}
