package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.example.popin.logic.NotificationPreferenceOptions;
import com.example.popin.logic.User;
import com.example.popin.logic.UserInputType;

import org.junit.Test;

public class UserValidationAndFactoryTest {

    @Test
    public void createUserWithEmail_setsEmailPreferenceAndTrimmedPassword() {
        User user = User.createUserWithEmail("user@example.com", "  pass123  ");

        assertEquals("user@example.com", user.getEmail());
        assertEquals("", user.getPhoneNumber());
        assertEquals("pass123", user.getPassword());
        assertEquals(NotificationPreferenceOptions.Email, user.getUserNotificationPreference());
    }

    @Test
    public void createUserWithPhoneNumber_setsSmsPreferenceAndTrimmedPassword() {
        User user = User.createUserWithPhoneNumber("+15145551234", "  pass123  ");

        assertEquals("", user.getEmail());
        assertEquals("+15145551234", user.getPhoneNumber());
        assertEquals("pass123", user.getPassword());
        assertEquals(NotificationPreferenceOptions.SMS, user.getUserNotificationPreference());
    }

    @Test
    public void identify_returnsExpectedInputType() {
        assertEquals(UserInputType.EMAIL, User.identify("person@example.com"));
        assertEquals(UserInputType.PHONE, User.identify("+15145551234"));
        assertEquals(UserInputType.UNKNOWN, User.identify("not-an-email-or-phone"));
        assertEquals(UserInputType.UNKNOWN, User.identify(null));
    }

    @Test
    public void setUserNotificationPreference_rejectsInvalidOrMissingContactInfo() {
        User noContactUser = new User("  ", "pw");
        assertEquals("Invalid preference selected", noContactUser.setUserNotificationPreference(null));
        assertEquals(
                "No email address found, please add an email to use this preference",
                noContactUser.setUserNotificationPreference(NotificationPreferenceOptions.Email)
        );
        assertEquals(
                "No phone number found, please add a phone number to use this preference",
                noContactUser.setUserNotificationPreference(NotificationPreferenceOptions.SMS)
        );
        assertNull(noContactUser.getUserNotificationPreference());
    }

    @Test
    public void emailAndPhoneValidation_acceptsAndRejectsExpectedFormats() {
        assertTrue(User.isValidEmail("valid.user+tag@example.com"));
        assertFalse(User.isValidEmail("invalid-email"));
        assertFalse(User.isValidEmail(""));

        assertTrue(User.isValidPhoneNumber("+15145551234"));
        assertFalse(User.isValidPhoneNumber("5145551234"));
        assertFalse(User.isValidPhoneNumber(""));
    }

    @Test
    public void setters_normalizeEmailPhoneAndPassword() {
        User user = new User("initial@example.com", "initial");

        user.setEmail("  USER@EXAMPLE.COM ");
        user.setPhoneNumber("  +15145551234 ");
        user.setPassword("  next-pass  ");

        assertEquals("user@example.com", user.getEmail());
        assertEquals("+15145551234", user.getPhoneNumber());
        assertEquals("next-pass", user.getPassword());
    }
}

