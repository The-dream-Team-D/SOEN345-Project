package com.example.popin.logic;

public enum EventCategory {
    SOCIAL,
    EDUCATIONAL,
    PROFESSIONAL,
    SPORTS,
    ENTERTAINMENT;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}

