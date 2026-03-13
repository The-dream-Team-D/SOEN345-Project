package com.example.popin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    public void Login(View view) {

        TextInputEditText emailInputField = findViewById(R.id.EmailInput);
        TextInputEditText passwordInputField = findViewById(R.id.passwordInput);

        String email_or_phoneNumber = emailInputField.getText().toString();
        String password = passwordInputField.getText().toString();

        User user = new User(email_or_phoneNumber, password);
        user.login(new User.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                // Login successful
                Toast.makeText(getApplicationContext(), "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();

                // Proceed to next activity
                Intent intent = new Intent(MainActivity.this, EventsPageActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                switch (message) {
                    case "Email/Phone input is Empty":
                        emailInputField.setError("Input is Empty");
                        break;
                    case "Password input is Empty":
                        passwordInputField.setError("Input is Empty");
                        break;
                    case "No user with that email/phone number":
                        emailInputField.setError("No registered account with this email/phone number");
                        break;
                    case "Incorrect password":
                        passwordInputField.setError("Incorrect password");
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
