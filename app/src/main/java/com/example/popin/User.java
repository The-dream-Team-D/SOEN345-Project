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


    // apparently required by firebase
    public User() {}

    public User(String email, String password) {

        this.email = email;
        this.password = password;
        this.address = "";
        this.name = "";
        this.isAdmin = false;
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
    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public void login(LoginCallback callback){
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        Query query = usersRef.orderByChild("email").equalTo(this.email);

        if(this.email == null || this.email.trim().isEmpty()) {
            callback.onError("Email/Phone input is Empty");
            return;
        }

        if(this.password == null || this.password.trim().isEmpty()) {
            callback.onError("Password input is Empty");
            return;
        }

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

                        User.this.setName(userSnapshot.child("name").getValue(String.class));
                        User.this.setAddress(userSnapshot.child("address").getValue(String.class));
                        User.this.setIsAdmin(Boolean.TRUE.equals(userSnapshot.child("isAdmin").getValue(boolean.class)));

                        callback.onSuccess(User.this);
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
