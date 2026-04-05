package com.example.popin.logic;

public enum NotificationPreferenceOptions {
    EMAIL("Email"),
    SMS("SMS");

    private final String displayName;

    NotificationPreferenceOptions(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

