package com.example.popin.uipages;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.popin.R;
import com.example.popin.logic.User;
import com.example.popin.logic.UserInSession;

public class LogInActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FF8559"));

        EditText passwordInputField = findViewById(R.id.password_input);
        ImageView passwordToggle = findViewById(R.id.eye_password_icon);
        TextView forgotPassword = findViewById(R.id.forgotPassword);

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


        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }




    public void Login(View view) {

        EditText emailInputField = findViewById(R.id.emailInput);
        EditText passwordInputField = findViewById(R.id.password_input);

        String emailOrPhoneNumber = emailInputField.getText().toString();
        String password = passwordInputField.getText().toString();

        User.login(emailOrPhoneNumber, password, new User.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(getApplicationContext(), "Welcome " + user.getName(), Toast.LENGTH_SHORT).show();
                UserInSession.create(user);

                Intent intent = new Intent(LogInActivity.this, EventsPageActivity.class);
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

