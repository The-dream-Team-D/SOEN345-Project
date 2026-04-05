package com.example.popin.logic;

public class UserInSession {
    private User currentUser;
    private static UserInSession instance;

    private UserInSession(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }
    public static void create(User user) {
        instance = new UserInSession(user);
    }
    public static UserInSession getInstance() {
        return instance;
    }

    public static void clear() {
        instance = null;
    }

    public static void updateUser(User user) {
        if (instance != null) {
            instance.currentUser = user;
        }
    }




}

