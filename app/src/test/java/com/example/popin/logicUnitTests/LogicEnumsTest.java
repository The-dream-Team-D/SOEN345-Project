package com.example.popin.logicUnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.popin.logic.EventFilterDateType;
import com.example.popin.logic.NotificationPreferenceOptions;
import com.example.popin.logic.NotificationType;
import com.example.popin.logic.UserInputType;

import org.junit.Test;

import java.util.Arrays;

public class LogicEnumsTest {

    @Test
    public void notificationType_containsExpectedValues() {
        assertEquals(5, NotificationType.values().length);
        assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.RegisterEvent));
        assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.RegisterAccount));
        assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.DeleteAccount));
        assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.CancelTicket));
        assertTrue(Arrays.asList(NotificationType.values()).contains(NotificationType.ChangePassword));
    }

    @Test
    public void eventFilterDateType_containsExpectedValues() {
        assertEquals(3, EventFilterDateType.values().length);
        assertTrue(Arrays.asList(EventFilterDateType.values()).contains(EventFilterDateType.PAST));
        assertTrue(Arrays.asList(EventFilterDateType.values()).contains(EventFilterDateType.UPCOMING));
        assertTrue(Arrays.asList(EventFilterDateType.values()).contains(EventFilterDateType.ALL));
    }

    @Test
    public void notificationPreferenceOptions_containsExpectedValues() {
        assertEquals(2, NotificationPreferenceOptions.values().length);
        assertTrue(Arrays.asList(NotificationPreferenceOptions.values()).contains(NotificationPreferenceOptions.Email));
        assertTrue(Arrays.asList(NotificationPreferenceOptions.values()).contains(NotificationPreferenceOptions.SMS));
    }

    @Test
    public void userInputType_containsExpectedValues() {
        assertEquals(3, UserInputType.values().length);
        assertTrue(Arrays.asList(UserInputType.values()).contains(UserInputType.EMAIL));
        assertTrue(Arrays.asList(UserInputType.values()).contains(UserInputType.PHONE));
        assertTrue(Arrays.asList(UserInputType.values()).contains(UserInputType.UNKNOWN));
    }
}

