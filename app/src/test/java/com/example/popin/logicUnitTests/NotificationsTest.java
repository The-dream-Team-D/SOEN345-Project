package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.popin.logic.Notifications;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class NotificationsTest {

    @Test
    public void buildCode_returnsSixCharacterString() {
        String code = Notifications.buildCode();

        assertEquals(6, code.length());
    }

    @Test
    public void buildCode_containsOnlyAlphaNumericCharacters() {
        String code = Notifications.buildCode();

        for (char c : code.toCharArray()) {
            assertTrue(Character.isLetterOrDigit(c));
        }
    }

    @Test
    public void buildCode_multipleCallsProduceNonConstantValues() {
        Set<String> generatedCodes = new HashSet<>();

        for (int i = 0; i < 25; i++) {
            generatedCodes.add(Notifications.buildCode());
        }

        assertTrue(generatedCodes.size() > 1);
    }
}

