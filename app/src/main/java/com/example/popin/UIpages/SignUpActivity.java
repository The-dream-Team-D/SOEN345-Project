package com.example.popin.UIpages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.popin.R;
import com.example.popin.logic.User;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FF8559"));

        EditText passwordInputField = findViewById(R.id.password_input);
        ImageView passwordToggle = findViewById(R.id.eye_password_icon);

        passwordToggle.setOnClickListener(v -> {
            if (passwordInputField.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                passwordInputField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordToggle.setImageResource(R.drawable.closed_eye_pass_toggle);
            } else {
                passwordInputField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                passwordToggle.setImageResource(R.drawable.eye_password_ic);
            }

            passwordInputField.setSelection(passwordInputField.getText().length());
        });
    }

    public void SignUp(View view) {

        EditText nameInputField = findViewById(R.id.NameInput);
        EditText emailInputField = findViewById(R.id.emailInput);
        EditText passwordInputField = findViewById(R.id.password_input);

        String name = nameInputField.getText().toString();
        String email_or_phoneNumber = emailInputField.getText().toString();
        String password = passwordInputField.getText().toString();

        User.SignUp(name, email_or_phoneNumber, password, new User.SignUpCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getApplicationContext(), "Account Created!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String message) {
                switch (message) {

                    case "Name input is empty":
                        nameInputField.setError("Input is Empty");
                        break;
                    case "Email/Phone input is Empty":
                        emailInputField.setError("Input is Empty");
                        break;
                    case "Password input is Empty":
                        passwordInputField.setError("Input is Empty");
                        break;
                    case "Must be a valid email or phone number":
                        emailInputField.setError("Not valid Email/Phone Number");
                        break;
                    case "An account with this Email/Phone Number already exists":
                        emailInputField.setError("This Email/Phone number is already in use");
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
