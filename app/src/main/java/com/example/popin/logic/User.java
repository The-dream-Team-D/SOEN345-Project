package com.example.popin.logic;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class User {

    private String email;
    private String password;
    private String address;
    private String name;
    private boolean isAdmin = false;

    private String phoneNumber;

    private NotificationPreferenceOptions userNotificationPreference;


    // apparently required by firebase
    public User() {}

    public User(String email, String password) {

        this.email = email;
        this.password = password;
        this.address = "";
        this.name = "";
        this.isAdmin = false;
        this.phoneNumber = "";
        this.userNotificationPreference = NotificationPreferenceOptions.Email;
    }

    private User(String email, String phoneNumber, String password, NotificationPreferenceOptions pref) {
        this.email = email;
        this.password = password;
        this.address = "";
        this.name = "";
        this.isAdmin = false;
        this.phoneNumber = phoneNumber;
        this.userNotificationPreference = pref;
    }

    public static User createUserWithEmail(String email, String password){
        return new User(email, "", password, NotificationPreferenceOptions.Email);
    }

    public static User createUserWithPhoneNumber(String phoneNumber, String password){
        return new User("", phoneNumber, password, NotificationPreferenceOptions.SMS);
    }

    //setters
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserNotificationPreference(NotificationPreferenceOptions pref) {
        this.userNotificationPreference = pref;
    }


    //getters

    public String getAddress() {
        return this.address;
    }
    public String getPassword() {
        return this.password;
    }
    public String getEmail() {
        return this.email;
    }
    public String getName() {
        return this.name;
    }
    public boolean getIsAdmin() {
        return this.isAdmin;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public NotificationPreferenceOptions getUserNotificationPreference() {
        return this.userNotificationPreference;
    }

    public static boolean isValidEmail(String input) {
        if (input == null || input.isBlank()) return false;
        return input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidPhoneNumber(String input) {
        if (input == null || input.isBlank()) return false;
        return input.matches("^\\+[1-9]\\d{6,14}$");
    }

    public static UserInputType identify(String input) {
        if (isValidEmail(input))       return UserInputType.EMAIL;
        if (isValidPhoneNumber(input)) return UserInputType.PHONE;
        return UserInputType.UNKNOWN;
    }

    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public static void login(String email_or_phoneNumber, String password, LoginCallback callback){

        User user = null;

        if(email_or_phoneNumber == null || email_or_phoneNumber.trim().isEmpty()) {
            callback.onError("Email/Phone input is Empty");
            return;
        }

        if(password == null || password.trim().isEmpty()) {
            callback.onError("Password input is Empty");
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = null;
        UserInputType type = identify(email_or_phoneNumber);

        if (type == UserInputType.EMAIL) {
            user = createUserWithEmail(email_or_phoneNumber, password);
            query = usersRef.orderByChild("email").equalTo(user.email);

        } else if (type == UserInputType.PHONE) {
            user = createUserWithPhoneNumber(email_or_phoneNumber, password);
            query = usersRef.orderByChild("phoneNumber").equalTo(user.phoneNumber);
        } else {
            callback.onError("Must be a valid email or phone number");
            return;
        }

        final User finalUser = user;


        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onError("No user with that email/phone number");
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    String dbPassword = userSnapshot.child("password").getValue(String.class);

                    if (password.equals(dbPassword)) {
                        System.out.println("Login successful");

                        finalUser.setName(userSnapshot.child("name").getValue(String.class));
                        finalUser.setAddress(userSnapshot.child("address").getValue(String.class));
                        finalUser.setIsAdmin(Boolean.TRUE.equals(userSnapshot.child("isAdmin").getValue(boolean.class)));
                        finalUser.setPhoneNumber(userSnapshot.child("phoneNumber").getValue(String.class));
                        String prefString = snapshot.child("userNotificationPreference").getValue(String.class);
                        if (prefString != null) {
                            finalUser.setUserNotificationPreference(
                                    NotificationPreferenceOptions.valueOf(prefString)
                            );
                        }


                        callback.onSuccess(finalUser);
                        return;

                    } else {
                        callback.onError("Incorrect password");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Database error: " + error.getMessage());
            }
        });
    }
}
