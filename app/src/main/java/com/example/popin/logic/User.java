package com.example.popin.logic;

import static com.example.popin.logic.Notifications.sendNotification;

import com.example.popin.addedfiles.Admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class User {
    private static final String USERS_NODE = "Users";
    private static final String EMAIL_FIELD = "email";
    private static final String PHONE_FIELD = "phoneNumber";
    private static final String PASSWORD_FIELD = "password";
    private static final String ADDRESS_FIELD = "address";
    private static final String NOTIF_PREF_FIELD = "NotificationPreference";
    private static final String PASSWORD_EMPTY_ERROR = "Password input is Empty";
    private static final String NO_USER_ERROR = "No user with that email/phone number";
    private static final String EMAIL_PHONE_EMPTY_ERROR = "Email/Phone input is Empty";

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
        return new User(email, "", password == null ? "" : password.trim(), NotificationPreferenceOptions.EMAIL);
    }

    public static User createUserWithPhoneNumber(String phoneNumber, String password){
        return new User("", phoneNumber, password == null ? "" : password.trim(), NotificationPreferenceOptions.SMS);
    }

    //setters
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPassword(String password) {
        this.password = password == null ? "" : password.trim();
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
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber != null){
            this.phoneNumber = phoneNumber.toLowerCase().trim();
        }
    }
    public void setBio(String bio) { this.bio = bio; }

    public String getBio() { return this.bio; }

    public String setUserNotificationPreference(NotificationPreferenceOptions pref) {
        if (pref == null) {
            return "Invalid preference selected";
        }

        switch(pref) {
            case EMAIL:
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

    private static NotificationPreferenceOptions safeParsePreference(String prefString) {
        if (prefString == null) {
            return null;
        }
        try {
            return NotificationPreferenceOptions.valueOf(prefString);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static void signUp(String name, String emailOrPhoneNumber, String password, GenericCallback callback){
        if (name == null || name.trim().isEmpty()) {
            callback.onError("Name input is empty");
            return;
        }

        if (emailOrPhoneNumber == null || emailOrPhoneNumber.trim().isEmpty()) {
            callback.onError(EMAIL_PHONE_EMPTY_ERROR);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            callback.onError(PASSWORD_EMPTY_ERROR);
            return;
        }

        User user = null;
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        Query query = null;
        UserInputType type = identify(emailOrPhoneNumber);
        String normalizedEmailOrPhone = emailOrPhoneNumber.toLowerCase().trim();

        if (type == UserInputType.EMAIL){
            user = createUserWithEmail(normalizedEmailOrPhone, password);
            query = usersRef.orderByChild(EMAIL_FIELD).equalTo(user.email);

        } else if (type == UserInputType.PHONE) {
            user = createUserWithPhoneNumber(normalizedEmailOrPhone, password);
            query = usersRef.orderByChild(PHONE_FIELD).equalTo(user.phoneNumber);
        } else {
            callback.onError("Must be a valid email or phone number");
            return;
        }

        user.setName(name);

        final User finalUser = user;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onError("An account with this Email/Phone Number already exists");
                    return;
                }


                Map<String, Object> userData = new HashMap<>();
                userData.put(EMAIL_FIELD, finalUser.getEmail());
                userData.put(PHONE_FIELD, finalUser.getPhoneNumber());

                userData.put(PASSWORD_FIELD, finalUser.getPassword());
                userData.put(ADDRESS_FIELD, finalUser.getAddress());
                userData.put("name", finalUser.getName());

                userData.put("isAdmin", finalUser.getIsAdmin());
                userData.put(NOTIF_PREF_FIELD, finalUser.getUserNotificationPreference());

                usersRef.push().setValue(userData)
                        .addOnSuccessListener(aVoid -> {
                            sendNotification(finalUser, "", NotificationType.REGISTER_ACCOUNT,"");
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

    public static void login(String emailOrPhoneNumber, String password, LoginCallback callback){
        if(emailOrPhoneNumber == null || emailOrPhoneNumber.trim().isEmpty()) {
            callback.onError(EMAIL_PHONE_EMPTY_ERROR);
            return;
        }
        if(password == null || password.trim().isEmpty()) {
            callback.onError(PASSWORD_EMPTY_ERROR);
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        UserInputType type = identify(emailOrPhoneNumber);
        String normalizedEmailOrPhone = emailOrPhoneNumber.toLowerCase().trim();

        User user = (type == UserInputType.PHONE)
                ? createUserWithPhoneNumber(normalizedEmailOrPhone, password)
                : createUserWithEmail(normalizedEmailOrPhone, password);
        Query query = (type == UserInputType.PHONE)
                ? usersRef.orderByChild(PHONE_FIELD).equalTo(user.phoneNumber)
                : usersRef.orderByChild(EMAIL_FIELD).equalTo(user.email);

        final User finalUser = user;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(NO_USER_ERROR);
                    return;
                }
                processLoginSnapshot(snapshot, finalUser, password, callback);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }

    private static void processLoginSnapshot(DataSnapshot snapshot, User finalUser, String password, LoginCallback callback) {
        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
            String dbPassword = userSnapshot.child(PASSWORD_FIELD).getValue(String.class);
            if (password.equals(dbPassword)) {
                populateUserFromSnapshot(finalUser, userSnapshot);
                User result = finalUser.getIsAdmin() ? new Admin(finalUser) : finalUser;
                callback.onSuccess(result);
                return;
            } else {
                callback.onError("Incorrect password");
            }
        }
    }

    private static void populateUserFromSnapshot(User user, DataSnapshot snapshot) {
        user.setName(snapshot.child("name").getValue(String.class));
        user.setAddress(snapshot.child(ADDRESS_FIELD).getValue(String.class));
        user.setIsAdmin(Boolean.TRUE.equals(snapshot.child("isAdmin").getValue(boolean.class)));
        user.setPhoneNumber(snapshot.child(PHONE_FIELD).getValue(String.class));
        user.setBio(snapshot.child("bio").getValue(String.class));
        String prefString = snapshot.child(NOTIF_PREF_FIELD).getValue(String.class);
        NotificationPreferenceOptions pref = safeParsePreference(prefString);
        if (pref != null) {
            user.setUserNotificationPreference(pref);
        }
    }

    public void updateProfile(GenericCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        Query query = usersRef.orderByChild(EMAIL_FIELD).equalTo(this.email);

        final User finalUser = this;

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onError("User not found");
                    return;
                }

                boolean updated = false;


                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    userSnapshot.getRef().child("name").setValue(finalUser.getName());

                    userSnapshot.getRef().child(ADDRESS_FIELD).setValue(finalUser.getAddress());

                    userSnapshot.getRef().child("phoneNumber").setValue(finalUser.getPhoneNumber());

                    userSnapshot.getRef().child("bio").setValue(finalUser.getBio());

                    NotificationPreferenceOptions pref = finalUser.getUserNotificationPreference();
                    userSnapshot.getRef().child(NOTIF_PREF_FIELD)
                            .setValue(pref == null ? null : pref.name());
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

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(USERS_NODE);

        Query query = null;
        if(this.getEmail() != null && !this.getEmail().trim().isEmpty()) {
            query = usersRef.orderByChild(EMAIL_FIELD).equalTo(this.getEmail());
        }else{
            query = usersRef.orderByChild(PHONE_FIELD).equalTo(this.getPhoneNumber());
        }

        final User finalUser = this;
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
                            sendNotification(finalUser, "", NotificationType.DELETE_ACCOUNT, "");
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


    public static void forgotPassword(String emailOrPhoneNumber, String password, LoginCallback callback){
        if(emailOrPhoneNumber == null || emailOrPhoneNumber.trim().isEmpty()) {
            callback.onError(EMAIL_PHONE_EMPTY_ERROR);
            return;
        }
        if(password == null || password.trim().isEmpty()) {
            callback.onError("New Password input is Empty");
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        UserInputType type = identify(emailOrPhoneNumber);
        String normalizedEmailOrPhone = emailOrPhoneNumber.toLowerCase().trim();

        User user = (type == UserInputType.PHONE)
                ? createUserWithPhoneNumber(normalizedEmailOrPhone, password)
                : createUserWithEmail(normalizedEmailOrPhone, password);
        Query query = (type == UserInputType.PHONE)
                ? usersRef.orderByChild(PHONE_FIELD).equalTo(user.phoneNumber)
                : usersRef.orderByChild(EMAIL_FIELD).equalTo(user.email);

        final User finalUser = user;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError(NO_USER_ERROR);
                    return;
                }
                processPasswordResetSnapshot(snapshot, finalUser, password, callback);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }

    private static void processPasswordResetSnapshot(DataSnapshot snapshot, User finalUser, String newPassword, LoginCallback callback) {
        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
            String dbPassword = userSnapshot.child(PASSWORD_FIELD).getValue(String.class);
            if (newPassword.equals(dbPassword)) {
                callback.onError("New password can't be the same as old password");
                return;
            }
            String phoneNumString = userSnapshot.child(PHONE_FIELD).getValue(String.class);
            String prefString = userSnapshot.child(NOTIF_PREF_FIELD).getValue(String.class);
            finalUser.setPhoneNumber(phoneNumString);
            NotificationPreferenceOptions pref = safeParsePreference(prefString);
            if (pref != null) {
                finalUser.setUserNotificationPreference(pref);
            }
            callback.onSuccess(finalUser);
        }
    }



    public void changePassword(GenericCallback callback){

        String emailOrPhoneNumber;
        if (this.getEmail() != null && !this.getEmail().trim().isEmpty()) {
            emailOrPhoneNumber = this.getEmail();
        } else if (this.getPhoneNumber() != null && !this.getPhoneNumber().trim().isEmpty()) {
            emailOrPhoneNumber = this.getPhoneNumber();
        } else {
            callback.onError(EMAIL_PHONE_EMPTY_ERROR);
            return;
        }

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference(USERS_NODE);
        UserInputType type = identify(emailOrPhoneNumber);
        String normalizedEmailOrPhone = emailOrPhoneNumber.toLowerCase().trim();
        Query query;

        if (type == UserInputType.PHONE) {
            query = usersRef.orderByChild(PHONE_FIELD).equalTo(normalizedEmailOrPhone);
        } else {
            query = usersRef.orderByChild(EMAIL_FIELD).equalTo(normalizedEmailOrPhone);
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onError(NO_USER_ERROR);
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    userSnapshot.getRef().child(PASSWORD_FIELD).setValue(password)
                            .addOnSuccessListener(unused -> {
                                callback.onSuccess("Password Changed Successfully!");
                            })
                            .addOnFailureListener(e -> {
                                callback.onError("Failed to update password: " + e.getMessage());
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }


}


