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
    private String phone;
    private String bio;
    private boolean isAdmin = false;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.address = "";
        this.name = "";
        this.phone = "";
        this.bio = "";
        this.isAdmin = false;
    }


    public void setAddress(String address) { this.address = address; }
    public void setPassword(String password) { this.password = password; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBio(String bio) { this.bio = bio; }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }


    public String getAddress() { return this.address; }
    public String getPassword() { return this.password; }
    public String getEmail() { return this.email; }
    public String getName() { return this.name; }
    public String getPhone() { return this.phone; }
    public String getBio() { return this.bio; }
    public boolean getIsAdmin() { return this.isAdmin; }


    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface UpdateCallback {
        void onSuccess();
        void onError(String message);
    }


    public void login(LoginCallback callback) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");


        if (this.email == null || this.email.trim().isEmpty()) {
            callback.onError("Email/Phone input is Empty");
            return;
        }

        if (this.password == null || this.password.trim().isEmpty()) {
            callback.onError("Password input is Empty");
            return;
        }

        Query query = usersRef.orderByChild("email").equalTo(this.email);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {


                if (!snapshot.exists()) {
                    callback.onError("No user with that email/phone number");
                    return;
                }

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {

                    String dbPassword = userSnapshot.child("password").getValue(String.class);


                    if (dbPassword != null && password.equals(dbPassword)) {

                        User.this.setName(userSnapshot.child("name").getValue(String.class));
                        User.this.setAddress(userSnapshot.child("address").getValue(String.class));
                        User.this.setPhone(userSnapshot.child("phone").getValue(String.class));
                        User.this.setBio(userSnapshot.child("bio").getValue(String.class));
                        User.this.setIsAdmin(Boolean.TRUE.equals(
                                userSnapshot.child("isAdmin").getValue(Boolean.class)
                        ));

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

                boolean updated = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    userSnapshot.getRef().child("name").setValue(name);
                    userSnapshot.getRef().child("address").setValue(address);
                    userSnapshot.getRef().child("phone").setValue(phone);
                    userSnapshot.getRef().child("bio").setValue(bio);
                    updated = true;
                }

                if (updated) {
                    callback.onSuccess();
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
}