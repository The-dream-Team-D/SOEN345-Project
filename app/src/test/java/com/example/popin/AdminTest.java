package com.example.popin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Date;

public class AdminTest {

    @Test
    public void admin_extendsUser_andStoresId() {
        Admin admin = new Admin("admin@example.com", "secret", "firebase-key-42");
        assertTrue(admin instanceof User);
        assertEquals("firebase-key-42", admin.getId());
        admin.setId("new-id");
        assertEquals("new-id", admin.getId());
    }

    @Test
    public void admin_inheritsUserFields() {
        Admin admin = new Admin("a@b.com", "pw", "id1");
        admin.setName("Site Admin");
        admin.setAddress("HQ");
        admin.setIsAdmin(true);
        assertEquals("Site Admin", admin.getName());
        assertEquals("HQ", admin.getAddress());
        assertEquals("a@b.com", admin.getEmail());
        assertTrue(admin.getIsAdmin());
    }

    @Test
    public void event_constructor_usedByAdminDashboard_hasExpectedFields() {
        Date when = new Date();
        Event e = new Event("Title", "Venue", "Details", when, EventCategory.CONCERT);
        assertEquals("Title", e.getName());
        assertEquals("Venue", e.getLocation());
        assertEquals(EventCategory.CONCERT, e.getEventCategory());
        assertTrue(e.isAvailable());
    }
}
