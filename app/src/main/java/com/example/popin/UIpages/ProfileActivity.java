package com.example.popin.UIpages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popin.R;
import com.example.popin.logic.UserInSession;
import com.example.popin.reusableUI.NavBarComponentView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        View navBar = findViewById(R.id.bottomNav);
        NavBarComponentView.setup(navBar);


        ImageView questionMarks = findViewById(R.id.question_marks);
        Button loginButton = findViewById(R.id.LoginToViewProfileOptions);

        UserInSession session = UserInSession.getInstance();
        if (session == null || session.getUser() == null) {

            questionMarks.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);

            loginButton.setOnClickListener(v ->
                    startActivity(new Intent(this, LogInActivity.class)));


        } else {

            questionMarks.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);

        }
    }
}
