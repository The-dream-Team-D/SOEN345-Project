package com.example.popin.logic;

import static com.example.popin.logic.Notifications.sendNotification;

import android.util.Log;

import com.example.popin.addedFiles.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String email;
    private String password;
    private String address;
    private String name;
    private boolean isAdmin = false;

    private String phoneNumber;

    private NotificationPreferenceOptions userNotificationPreference;
    private String bio;



    // apparently required by firebase
    public User() {}

    public User(String email, String password) {

        this.email = email;
        this.password = password;
        this.address = "";
        this.name = "";
        this.isAdmin = false;
        this.phoneNumber = "";
        this.userNotificationPreference = null;
        this.bio = "";
    }

    private User(String email, String phoneNumber, String password, NotificationPreferenceOptions pref) {
        this.email = email;
        this.password = password;
        this.address = "";
        this.name = "";
        this.isAdmin = false;
        this.phoneNumber = phoneNumber;
        this.userNotificationPreference = pref;
        this.bio = "";
    }

    public static User createUserWithEmail(String email, String password){
        return new User(email, "", password.trim(), NotificationPreferenceOptions.Email);
    }

    public static User createUserWithPhoneNumber(String phoneNumber, String password){
        return new User("", phoneNumber, password.trim(), NotificationPreferenceOptions.SMS);
    }

    //setters
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPassword(String password) {
        this.password = password.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setEmail(String email) {
        if(email != null){
        this.email = email.toLowerCase().trim();
        }
        else{
            this.email = email;
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber != null){
            this.phoneNumber = phoneNumber.toLowerCase().trim();
        }
        else{
            this.phoneNumber = phoneNumber;
        }
    }
    public void setBio(String bio) { this.bio = bio; }

    public String getBio() { return this.bio; }

    public String setUserNotificationPreference(NotificationPreferenceOptions pref) {
        if (pref == null) {
            return "Invalid preference selected";
        }

        switch(pref) {
            case Email:
                if (this.email == null || this.email.trim().isEmpty()) {
                    return "No email address found, please add an email to use this preference";
                }
                break;
            case SMS:
                if (this.phoneNumber == null || this.phoneNumber.trim().isEmpty()) {
                    return "No phone number found, please add a phone number to use this preference";
                }
                break;
        }

        this.userNotificationPreference = pref;
        return "Preference updated successfully";
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

    public static void SignUp(String name, String email_or_phoneNumber, String password, GenericCallback callback){
        if (name == null || name.trim().isEmpty()) {
            callback.onError("Name input is empty");
            return;
        }

        if (email_or_phoneNumber == null || email_or_phoneNumber.trim().isEmpty()) {
            callback.onError("Email/Phone input is Empty");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            callback.onError("Password input is Empty");
            return;
        }

        User user = null;
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = null;
        UserInputType type = identify(email_or_phoneNumber);
        String normalizedEmailOrPhone = email_or_phoneNumber.toLowerCase().trim();

        if (type == UserInputType.EMAIL){
            user = createUserWithEmail(normalizedEmailOrPhone, password);
            query = usersRef.orderByChild("email").equalTo(user.email);

        } else if (type == UserInputType.PHONE) {
            user = createUserWithPhoneNumber(normalizedEmailOrPhone, password);
            query = usersRef.orderByChild("phoneNumber").equalTo(user.phoneNumber);
        } else {
            callback.onError("Must be a valid email or phone number");
            return;
        }

        user.setName(name);

        final User finaluser = user;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onError("An account with this Email/Phone Number already exists");
                    return;
                }


                Map<String, Object> userData = new HashMap<>();
                userData.put("email", finaluser.getEmail());
                userData.put("phoneNumber", finaluser.getPhoneNumber());

                userData.put("password", finaluser.getPassword());
                userData.put("address", finaluser.getAddress());
                userData.put("name", finaluser.getName());

                userData.put("isAdmin", finaluser.getIsAdmin());
                userData.put("NotificationPreference", finaluser.getUserNotificationPreference());

                usersRef.push().setValue(userData)
                        .addOnSuccessListener(aVoid -> {
                            sendNotification(finaluser, "", NotificationType.RegisterAccount,"");
                            callback.onSuccess("Success");
                        })
                        .addOnFailureListener(e -> {
                            callback.onError("Couldn't Push Values");
                        });

            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
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
        String normalizedEmailOrPhone = email_or_phoneNumber.toLowerCase().trim();



        // NO Need for input validation in login, either login or you cant. This just defines type to search
        if (type == UserInputType.PHONE) {
            user = createUserWithPhoneNumber(normalizedEmailOrPhone, password);
            query = usersRef.orderByChild("phoneNumber").equalTo(user.phoneNumber);
        } else {
            user = createUserWithEmail(normalizedEmailOrPhone, password);
            query = usersRef.orderByChild("email").equalTo(user.email);
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
                        finalUser.setBio(userSnapshot.child("bio").getValue(String.class));

                        String prefString = userSnapshot.child("NotificationPreference").getValue(String.class);

                        if (prefString != null) {
                            finalUser.setUserNotificationPreference(
                                    NotificationPreferenceOptions.valueOf(prefString)
                            );
                        }

                        if (finalUser.getIsAdmin()){
                            Admin admin = new Admin(finalUser);
                            callback.onSuccess(admin);
                            return;
                        }else {
                            callback.onSuccess(finalUser);
                            return;
                        }

                    } else {
                        callback.onError("Incorrect password");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }

    private boolean validateInputs(LoginCallback callback) {
        if (this.email == null || this.email.trim().isEmpty()) {
            callback.onError("Email/Phone input is Empty");
            return false;
        }

        if (this.password == null || this.password.trim().isEmpty()) {
            callback.onError("Password input is Empty");
            return false;
        }

        return true;
    }

    private void handleSnapshot(DataSnapshot snapshot, LoginCallback callback) {
        if (!snapshot.exists()) {
            callback.onError("No user with that email/phone number");
            return;
        }

        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
            if (processUser(userSnapshot, callback)) return;
        }
    }

    private boolean processUser(DataSnapshot userSnapshot, LoginCallback callback) {
        String dbPassword = userSnapshot.child("password").getValue(String.class);

        if (dbPassword != null && password.equals(dbPassword)) {

            String dbName = userSnapshot.child("name").getValue(String.class);
            String dbAddress = userSnapshot.child("address").getValue(String.class);

            DataSnapshot phoneSnap = userSnapshot.child("phone");
            DataSnapshot bioSnap = userSnapshot.child("bio");

            String dbPhone = phoneSnap != null ? phoneSnap.getValue(String.class) : null;
            String dbBio = bioSnap != null ? bioSnap.getValue(String.class) : null;

            Boolean isAdminValue = userSnapshot.child("isAdmin").getValue(Boolean.class);

            this.setName(dbName != null ? dbName : "");
            this.setAddress(dbAddress != null ? dbAddress : "");
            this.setPhoneNumber(dbPhone != null ? dbPhone : "");
            this.setBio(dbBio != null ? dbBio : "");
            this.setIsAdmin(Boolean.TRUE.equals(isAdminValue));

            callback.onSuccess(this);
            return true;
        }

        callback.onError("Incorrect password");
        return false;
    }

    public void updateProfile(GenericCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = usersRef.orderByChild("email").equalTo(this.email);

        final User Finaluser = this;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onError("User not found");
                    return;
                }

                boolean updated = false;


                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    userSnapshot.getRef().child("name").setValue(Finaluser.getName());

                    userSnapshot.getRef().child("address").setValue(Finaluser.getAddress());

                    userSnapshot.getRef().child("phoneNumber").setValue(Finaluser.getPhoneNumber());

                    userSnapshot.getRef().child("bio").setValue(Finaluser.getBio());

                    NotificationPreferenceOptions pref = Finaluser.getUserNotificationPreference();
                    userSnapshot.getRef().child("NotificationPreference")
                            .setValue(pref == null ? null : pref.toString());
                    updated = true;
                }
                if (updated) {
                    callback.onSuccess("User Profile Updated");
                } else {
                    callback.onError("User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }

    public void delete(GenericCallback callBack){

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        Query query = null;
        if(this.getEmail() != null && !this.getEmail().trim().isEmpty()) {
            query = usersRef.orderByChild("email").equalTo(this.getEmail());
        }else{
            query = usersRef.orderByChild("phoneNumber").equalTo(this.getPhoneNumber());
        }

        final User finaluser = this;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callBack.onError("Error encountered locating user in DB");
                    return;
                }

                DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();

                userSnapshot.getRef().removeValue()
                        .addOnSuccessListener(unused -> {
                            callBack.onSuccess("Deleted Account Successfully!");
                            sendNotification(finaluser, "", NotificationType.DeleteAccount, "");
                        })
                        .addOnFailureListener(e -> {
                            callBack.onError("Failed to Delete DB Account");
                        });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callBack.onError(error.getMessage());
            }
        });

    }


    public static void forgotPassword(String email_or_phoneNumber, String password, LoginCallback callback){

        User user = null;

        if(email_or_phoneNumber == null || email_or_phoneNumber.trim().isEmpty()) {
            callback.onError("Email/Phone input is Empty");
            return;
        }

        if(password == null || password.trim().isEmpty()) {
            callback.onError("New Password input is Empty");
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = null;
        UserInputType type = identify(email_or_phoneNumber);
        String normalizedEmailOrPhone = email_or_phoneNumber.toLowerCase().trim();



        // NO Need for input validation in login, either login or you cant. This just defines type to search
        if (type == UserInputType.PHONE) {
            user = createUserWithPhoneNumber(normalizedEmailOrPhone, password);
            query = usersRef.orderByChild("phoneNumber").equalTo(user.phoneNumber);
        } else {
            user = createUserWithEmail(normalizedEmailOrPhone, password);
            query = usersRef.orderByChild("email").equalTo(user.email);
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
                        callback.onError("New password can't be the same as old password");
                        return;

                    } else {

                        String phoneNumString = userSnapshot.child("phoneNumber").getValue(String.class);
                        String prefString = userSnapshot.child("NotificationPreference").getValue(String.class);

                        finalUser.setPhoneNumber(phoneNumString);
                        if (prefString != null) {
                            finalUser.setUserNotificationPreference(
                                    NotificationPreferenceOptions.valueOf(prefString)
                            );
                        }

                        callback.onSuccess(finalUser);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }



    public void changePassword(GenericCallback callback){

        String emailOrPhoneNumber;
        if (this.getEmail() != null && !this.getEmail().trim().isEmpty()) {
            emailOrPhoneNumber = this.getEmail();
        } else if (this.getPhoneNumber() != null && !this.getPhoneNumber().trim().isEmpty()) {
            emailOrPhoneNumber = this.getPhoneNumber();
        } else {
            callback.onError("Email/Phone input is Empty");
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        UserInputType type = identify(emailOrPhoneNumber);
        String normalizedEmailOrPhone = emailOrPhoneNumber.toLowerCase().trim();
        Query query;

        if (type == UserInputType.PHONE) {
            query = usersRef.orderByChild("phoneNumber").equalTo(normalizedEmailOrPhone);
        } else {
            query = usersRef.orderByChild("email").equalTo(normalizedEmailOrPhone);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onError("No user with that email/phone number");
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    userSnapshot.getRef().child("password").setValue(password)
                            .addOnSuccessListener(unused -> {
                                callback.onSuccess("Password Changed Successfully!");
                            })
                            .addOnFailureListener(e -> {
                                callback.onError("Failed to update password: " + e.getMessage());
                            });

                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }


}
