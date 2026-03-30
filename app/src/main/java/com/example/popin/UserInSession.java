package com.example.popin;

public class UserInSession {
    private static User currentUser;

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

    /**
     * Safe access for UI code when the user may not have logged in yet.
     */
    public static User getCurrentUser() {
        UserInSession session = getInstance();
        if (session == null) {
            return null;
        }
        return session.getUser();
    }

    public static void clear() {

        instance = null;
        currentUser = null;
    }

}
