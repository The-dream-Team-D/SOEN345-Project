package com.example.popin;

import java.util.Locale;

public final class FirebaseUserKey {
    private FirebaseUserKey() {
    }

    public static String sanitize(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return "unknown_user";
        }
        return raw
                .replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_")
                .replace("/", "_");
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
