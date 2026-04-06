package com.example.popin.logicUnitTests;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import com.example.popin.logic.NotificationType;
import com.example.popin.logic.Notifications;
import com.example.popin.logic.User;
import com.example.popin.logic.service.EmailServicer;
import com.example.popin.logic.service.SMServicer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class NotificationDispatchTest {

    private MockedStatic<EmailServicer> mockedEmail;
    private MockedStatic<SMServicer> mockedSms;

    @Before
    public void setUp() {
        mockedEmail = mockStatic(EmailServicer.class);
        mockedSms = mockStatic(SMServicer.class);
    }

    @After
    public void tearDown() {
        if (mockedEmail != null) {
            mockedEmail.close();
        }
        if (mockedSms != null) {
            mockedSms.close();
        }
    }

    @Test
    public void sendNotification_emailPreference_usesEmailServicer() {
        User user = User.createUserWithEmail("user@example.com", "pw");
        user.setName("Kevin");

        Notifications.sendNotification(user, "SOEN Mixer", NotificationType.REGISTER_EVENT, "");

        mockedEmail.verify(() ->
                EmailServicer.sendEmail(
                        eq("user@example.com"),
                        eq("Event Registration Confirmed"),
                        contains("SOEN Mixer")
                ));
    }

    @Test
    public void sendNotification_smsPreference_usesSmsServicer() {
        User user = User.createUserWithPhoneNumber("+15145551234", "pw");
        user.setName("Kevin");

        Notifications.sendNotification(user, "SOEN Mixer", NotificationType.CANCEL_TICKET, "");

        mockedSms.verify(() ->
                SMServicer.sendSMS(
                        eq("+15145551234"),
                        contains("SOEN Mixer")
                ));
    }

    @Test
    public void sendNotification_changePassword_includesVerificationCode() {
        User user = User.createUserWithEmail("user@example.com", "pw");
        user.setName("Kevin");

        Notifications.sendNotification(user, "", NotificationType.CHANGE_PASSWORD, "ABC123");

        mockedEmail.verify(() ->
                EmailServicer.sendEmail(
                        eq("user@example.com"),
                        eq("Password Reset Code"),
                        contains("ABC123")
                ));
    }
}

