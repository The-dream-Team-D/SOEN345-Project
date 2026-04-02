package com.example.popin;

import com.google.firebase.database.*;

public class User {

    private String email;
    private String password;
    private String address;
    private String name;
    private boolean isAdmin = false;

    // Required by Firebase
    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.address = "";
        this.name = "";
        this.isAdmin = false;
    }

    // SETTERS
    public void setAddress(String address) { this.address = address; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    // GETTERS
    public String getAddress() { return this.address; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
    public String getName() { return this.name; }
    public boolean getIsAdmin() { return this.isAdmin; }

    // LOGIN CALLBACK
    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    // UPDATE CALLBACK
    public interface UpdateCallback {
        void onSuccess();
        void onError(String message);
    }

    // LOGIN METHOD
    public void login(LoginCallback callback){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        Query query = usersRef.orderByChild("email").equalTo(this.email);

        if(this.email == null || this.email.trim().isEmpty()) {
            callback.onError("Email is empty");
            return;
        }

        if(this.password == null || this.password.trim().isEmpty()) {
            callback.onError("Password is empty");
            return;
        }

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onError("No user found");
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    String dbPassword = userSnapshot.child("password").getValue(String.class);

                    if (password.equals(dbPassword)) {

                        User.this.setName(userSnapshot.child("name").getValue(String.class));
                        User.this.setAddress(userSnapshot.child("address").getValue(String.class));
                        User.this.setIsAdmin(Boolean.TRUE.equals(userSnapshot.child("isAdmin").getValue(Boolean.class)));

                        callback.onSuccess(User.this);
                        return;

                    } else {
                        callback.onError("Incorrect password");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }


    public void updateProfile(UpdateCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        Query query = usersRef.orderByChild("email").equalTo(this.email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    callback.onError("User not found");
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    userSnapshot.getRef().child("name").setValue(name);
                    userSnapshot.getRef().child("address").setValue(address);

                    callback.onSuccess();
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onError(error.getMessage());
            }
        });
    }
}