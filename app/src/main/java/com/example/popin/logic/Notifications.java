package com.example.popin.logic;

import com.example.popin.api.EmailServicer;
import com.example.popin.api.SMServicer;
import java.security.SecureRandom;

public class Notifications {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static void sendNotification(User user, String eventTitle, NotificationType n, String code){
        String subject;
        String html;
        String message;

        switch (n) {
            case REGISTER_EVENT:
                subject = "Event Registration Confirmed";
                html = "Hi " + user.getName() + "!<br><br>You have successfully registered for " + eventTitle + ".";
                message = "Hi " + user.getName() + "!\n\nYou have successfully registered for " + eventTitle + ".";
                break;
            case REGISTER_ACCOUNT:
                subject = "Welcome to PopIn!";
                html = "Welcome " + user.getName() + "!<br><br>Your PopIn account has been successfully created.";
                message = "Welcome " + user.getName() + "!\n\nYour PopIn account has been successfully created.";
                break;
            case DELETE_ACCOUNT:
                subject = "Account Deleted";
                html = "We are sorry to see you go " + user.getName() + "!<br><br>Your account has been successfully deleted.";
                message = "We are sorry to see you go " + user.getName() + "!\n\nYour account has been successfully deleted.";
                break;
            case CANCEL_TICKET:
                subject = "Event Registration Cancelled";
                html = "Your registration for the event " + eventTitle + " has been cancelled.<br><br>"
                        + "If you did not perform this action, please contact us at support@example.com.";
                message = "Your registration for the event " + eventTitle + " has been cancelled.\n\n"
                        + "If you did not perform this action, please contact us at support@example.com.";
                break;
            case CHANGE_PASSWORD:
                subject = "Password Reset Code";
                html = "You requested a password reset for your account.<br><br>"
                        + "Your verification code is: <strong>" + code + "</strong><br><br>"
                        + "This code will expire in 10 minutes.<br><br>"
                        + "If you did not request this, please contact us at support@example.com.";
                message = "You requested a password reset for your account.\n\n"
                        + "Your verification code is: " + code + "\n\n"
                        + "This code will expire in 10 minutes.\n\n"
                        + "If you did not request this, please contact us at support@example.com.";
                break;
            default: throw new IllegalArgumentException("Code shouldn't ever reach here, switch case is exhaustive");
        }

        if (user.getUserNotificationPreference() == NotificationPreferenceOptions.SMS) {
            SMServicer.sendSMS(user.getPhoneNumber(), message);
        } else {
            EmailServicer.sendEmail(user.getEmail(), subject, html);
        }
    }


    public static String buildCode(){

        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();

    }
}

