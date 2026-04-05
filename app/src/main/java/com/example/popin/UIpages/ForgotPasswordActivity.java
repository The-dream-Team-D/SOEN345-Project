package com.example.popin.UIpages;

import static com.example.popin.logic.Notifications.sendNotification;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.popin.R;
import com.example.popin.logic.GenericCallback;
import com.example.popin.logic.NotificationType;
import com.example.popin.logic.Notifications;
import com.example.popin.logic.User;

public class ForgotPasswordActivity extends AppCompatActivity {

    User finalUser;
    String code;

    ConstraintLayout afterEmailCheck;
    ConstraintLayout beforeEmailCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_pass);

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


    public void sendResetCode(View view) {

        EditText emailInputField = findViewById(R.id.emailInput);
        EditText passwordInputField = findViewById(R.id.password_input);

        String emailOrPhoneNumber = emailInputField.getText().toString();
        String password = passwordInputField.getText().toString();

        finalUser = new User(emailOrPhoneNumber, password);

        User.forgotPassword(emailOrPhoneNumber, password, new User.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                Toast.makeText(getApplicationContext(), "Reset Code Notification Sent", Toast.LENGTH_SHORT).show();

                code = Notifications.buildCode();

                sendNotification(user, "", NotificationType.CHANGE_PASSWORD, code);

                beforeEmailCheck = findViewById(R.id.BeforeEmailCheck);
                afterEmailCheck = findViewById(R.id.AfterEmailCheck);

                beforeEmailCheck.setVisibility(View.GONE);
                afterEmailCheck.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                switch (message) {
                    case "Email/Phone input is Empty":
                        emailInputField.setError("Input is Empty");
                        break;
                    case "New Password input is Empty":
                        passwordInputField.setError("Input is Empty");
                        break;
                    case "No user with that email/phone number":
                        emailInputField.setError("No registered account with this email/phone number");
                        break;
                    case "New password can't be the same as old password":
                        passwordInputField.setError("Password can't be the same as old password");
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void changePassword(View view) {

        EditText resetCodeInputField = findViewById(R.id.resetCodeInput);

        String resetCode = resetCodeInputField.getText().toString();

        if(!resetCode.equals(code)){
            resetCodeInputField.setError("Wrong Reset Code");
        }else{

            finalUser.changePassword(new GenericCallback(){
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    beforeEmailCheck.setVisibility(View.VISIBLE);
                    afterEmailCheck.setVisibility(View.GONE);

                    Intent intent = new Intent(ForgotPasswordActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

}

