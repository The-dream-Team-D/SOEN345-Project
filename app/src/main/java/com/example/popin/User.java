package com.example.popin;

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

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.name = "";
        this.address = "";
        this.isAdmin = false;
    }

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

    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface RegisterCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    private interface ErrorCallback {
        void onError(String message);
    }

    private interface SnapshotHandler {
        void onSnapshot(DataSnapshot snapshot);
    }

    private static final String USERS_PATH = "Users";

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private DatabaseReference getUsersRef() {
        return FirebaseDatabase.getInstance().getReference(USERS_PATH);
    }

    private Query emailQuery(DatabaseReference usersRef) {
        return usersRef.orderByChild("email").equalTo(this.email);
    }

    private void queryByEmail(DatabaseReference usersRef, SnapshotHandler onSnapshot, ErrorCallback onError) {
        emailQuery(usersRef).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                onSnapshot.onSnapshot(snapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                onError.onError("Database error: " + error.getMessage());
            }
        });
    }

    private DataSnapshot firstChild(DataSnapshot snapshot) {
        DataSnapshot firstChild = null;
        for (DataSnapshot child : snapshot.getChildren()) {
            firstChild = child;
            break;
        }
        return firstChild;
    }

    private void assignProfile(User user, String name, String address, boolean admin) {
        user.setName(name);
        user.setAddress(address);
        user.setIsAdmin(admin);
    }

    private boolean validateCredentials(ErrorCallback callback) {
        if (isBlank(this.email)) {
            callback.onError("Email input is empty");
            return false;
        }

        if (isBlank(this.password)) {
            callback.onError("Password input is empty");
            return false;
        }

        return true;
    }

    public void login(LoginCallback callback) {
        if (callback == null) {
            return;
        }

        if (!validateCredentials(callback::onError)) {
            return;
        }

        DatabaseReference usersRef = getUsersRef();
        queryByEmail(usersRef, snapshot -> {
            if (!snapshot.exists()) {
                callback.onError("No user with that email");
                return;
            }

            DataSnapshot userSnapshot = firstChild(snapshot);
            if (userSnapshot == null) {
                callback.onError("No user with that email");
                return;
            }

            String dbPassword = userSnapshot.child("password").getValue(String.class);
            if (!password.equals(dbPassword)) {
                callback.onError("Incorrect password");
                return;
            }

            String dbName = userSnapshot.child("name").getValue(String.class);
            String dbAddress = userSnapshot.child("address").getValue(String.class);
            Boolean dbIsAdmin = userSnapshot.child("isAdmin").getValue(Boolean.class);

            if (Boolean.TRUE.equals(dbIsAdmin)) {
                Admin adminUser = new Admin(User.this.email, User.this.password, userSnapshot.getKey());
                assignProfile(adminUser, dbName, dbAddress, true);
                callback.onSuccess(adminUser);
                return;
            }

            assignProfile(User.this, dbName, dbAddress, false);
            callback.onSuccess(User.this);
        }, callback::onError);
    }

    public void register(String name, String address, RegisterCallback callback) {
        if (callback == null) {
            return;
        }

        createAccount(name, address, false,
                "User account created successfully",
                "Failed to create account: ",
                callback);
    }

    public void createAdminAccount(String name, String address, RegisterCallback callback) {
        if (callback == null) {
            return;
        }

        createAccount(name, address, true,
                "Admin account created successfully",
                "Failed to create admin account: ",
                callback);
    }

    private void createAccount(String name,
                               String address,
                               boolean adminAccount,
                               String successMessage,
                               String failurePrefix,
                               RegisterCallback callback) {
        if (!validateCredentials(callback::onError)) {
            return;
        }

        DatabaseReference usersRef = getUsersRef();
        queryByEmail(usersRef, snapshot -> {
            if (snapshot.exists()) {
                callback.onError("An account with this email already exists");
                return;
            }

            assignProfile(User.this, name, address, adminAccount);

            String userId = usersRef.push().getKey();
            if (userId == null) {
                callback.onError("Failed to generate user id");
                return;
            }

            usersRef.child(userId)
                    .setValue(User.this)
                    .addOnSuccessListener(unused ->
                            callback.onSuccess(successMessage))
                    .addOnFailureListener(e ->
                            callback.onError(failurePrefix + e.getMessage()));
        }, callback::onError);
    }
}
