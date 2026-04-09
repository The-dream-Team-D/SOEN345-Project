package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.popin.logic.Admin;
import com.example.popin.logic.NotificationPreferenceOptions;
import com.example.popin.logic.User;

import org.junit.Test;

public class AdminUserConversionTest {

    @Test
    public void adminConstructor_fromUserCopiesFieldsAndSetsAdminTrue() {
        User user = User.createUserWithEmail("user@example.com", "pw");
        user.setName("Kevin");
        user.setPhoneNumber("+15145551234");
        user.setAddress("123 Main");
        user.setBio("about");
        user.setUserNotificationPreference(NotificationPreferenceOptions.EMAIL);

        Admin admin = new Admin(user);

        assertEquals("Kevin", admin.getName());
        assertEquals("user@example.com", admin.getEmail());
        assertEquals("+15145551234", admin.getPhoneNumber());
        assertEquals("123 Main", admin.getAddress());
        assertEquals("about", admin.getBio());
        assertEquals(NotificationPreferenceOptions.EMAIL, admin.getUserNotificationPreference());
        assertTrue(admin.getIsAdmin());
    }
}

