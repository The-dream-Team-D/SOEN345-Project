package com.example.popin.logic;

import android.util.Log;

import com.example.popin.logic.service.EmailServicer;
import com.example.popin.logic.service.SMServicer;

public class Notifications {

    public static void sendEmailNotification(User user, String eventTitle, NotificationType n){
        String subject;
        String html;
        String message;

        switch (n) {
            case RegisterEvent:
                subject = "Event Registration Confirmed";
                html = "Hi " + user.getName() + "!<br>You have successfully registered for " + eventTitle + ".";
                message = "Hi " + user.getName() + "!\nYou have successfully registered for " + eventTitle + ".";
                break;
            case RegisterAccount:
                subject = "Welcome to PopIn!";
                html = "Welcome " + user.getName() + "!<br>Your PopIn account has been successfully created.";
                message = "Welcome " + user.getName() + "!\nYour PopIn account has been successfully created.";
                break;
            case DeleteAccount:
                subject = "Account Deleted";
                html = "We are sorry to see you go " + user.getName() + "!<br>Your account has been successfully deleted.";
                message = "We are sorry to see you go " + user.getName() + "!\nYour account has been successfully deleted.";
                break;
            default:
                subject = "";
                html = "";
                message = "";
                break;
        }

        if (user.getUserNotificationPreference() == NotificationPreferenceOptions.SMS) {
            SMServicer.sendSMS(user.getPhoneNumber(), message);
        } else {
            EmailServicer.sendEmail(user.getEmail(), subject, html);
        }


    }
}
