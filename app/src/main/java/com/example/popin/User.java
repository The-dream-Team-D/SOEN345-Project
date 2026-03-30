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

    public void login(LoginCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = usersRef.orderByChild("email").equalTo(this.email);

        if (this.email == null || this.email.trim().isEmpty()) {
            callback.onError("Email input is empty");
            return;
        }

        if (this.password == null || this.password.trim().isEmpty()) {
            callback.onError("Password input is empty");
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    callback.onError("No user with that email");
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String dbPassword = userSnapshot.child("password").getValue(String.class);

                    if (password.equals(dbPassword)) {
                        String dbName = userSnapshot.child("name").getValue(String.class);
                        String dbAddress = userSnapshot.child("address").getValue(String.class);
                        Boolean dbIsAdmin = userSnapshot.child("isAdmin").getValue(Boolean.class);

                        if (Boolean.TRUE.equals(dbIsAdmin)) {
                            Admin adminUser = new Admin(User.this.email, User.this.password, userSnapshot.getKey());
                            adminUser.setName(dbName);
                            adminUser.setAddress(dbAddress);
                            adminUser.setIsAdmin(true);
                            callback.onSuccess(adminUser);
                        } else {
                            User.this.setName(dbName);
                            User.this.setAddress(dbAddress);
                            User.this.setIsAdmin(false);
                            callback.onSuccess(User.this);
                        }
                        return;
                    } else {
                        callback.onError("Incorrect password");
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }

    public void register(String name, String address, RegisterCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        if (this.email == null || this.email.trim().isEmpty()) {
            callback.onError("Email input is empty");
            return;
        }

        if (this.password == null || this.password.trim().isEmpty()) {
            callback.onError("Password input is empty");
            return;
        }

        Query query = usersRef.orderByChild("email").equalTo(this.email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onError("An account with this email already exists");
                    return;
                }

                User.this.setName(name);
                User.this.setAddress(address);

                String userId = usersRef.push().getKey();
                if (userId == null) {
                    callback.onError("Failed to generate user id");
                    return;
                }

                usersRef.child(userId)
                        .setValue(User.this)
                        .addOnSuccessListener(unused ->
                                callback.onSuccess("User account created successfully"))
                        .addOnFailureListener(e ->
                                callback.onError("Failed to create account: " + e.getMessage()));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }

    public void createAdminAccount(String name, String address, RegisterCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        if (this.email == null || this.email.trim().isEmpty()) {
            callback.onError("Email input is empty");
            return;
        }

        if (this.password == null || this.password.trim().isEmpty()) {
            callback.onError("Password input is empty");
            return;
        }

        Query query = usersRef.orderByChild("email").equalTo(this.email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    callback.onError("An account with this email already exists");
                    return;
                }

                User.this.setName(name);
                User.this.setAddress(address);
                User.this.setIsAdmin(true);

                String userId = usersRef.push().getKey();
                if (userId == null) {
                    callback.onError("Failed to generate user id");
                    return;
                }

                usersRef.child(userId)
                        .setValue(User.this)
                        .addOnSuccessListener(unused ->
                                callback.onSuccess("Admin account created successfully"))
                        .addOnFailureListener(e ->
                                callback.onError("Failed to create admin account: " + e.getMessage()));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError("Database error: " + error.getMessage());
            }
        });
    }
}