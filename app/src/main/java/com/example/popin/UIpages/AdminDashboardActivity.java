package com.example.popin.UIpages;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.popin.R;
import com.example.popin.reusableUI.NavBarComponentView;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        View navBar = findViewById(R.id.bottomNav);
        NavBarComponentView.setup(navBar);

    }

}
