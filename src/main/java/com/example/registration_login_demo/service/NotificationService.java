package com.example.registration_login_demo.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.registration_login_demo.dto.Notification;
import com.example.registration_login_demo.dto.UserSettings;

public class NotificationService {
    private final EmailSender emailSender;
    private final String recipientEmail;
    private final UserSettings userSettings;

    public NotificationService(String recipientEmail) {
        this.emailSender = new EmailSender();
        this.recipientEmail = recipientEmail;
        this.userSettings = new UserSettings(recipientEmail);
    }

    // public void configureUserSettings(String email, double profitThreshold,
    // double lossThreshold,
    // boolean notificationsEnabled, Principal principal) {
    // this.userSettings = new UserSettings(principal.getName(), profitThreshold,
    // lossThreshold, notificationsEnabled);
    // }

    // public void configureUserSettings(String email, double profitThreshold,
    // double lossThreshold,
    // boolean notificationsEnabled, UserSettings userSettings) {
    // this.userSettings = new UserSettings(email,
    // userSettings.getProfitThreshold(), userSettings.getLossThreshold(),
    // notificationsEnabled);
    // }

    public void enableNotifications() {
        if (userSettings != null) {
            userSettings.setNotificationsEnabled(true);
        }
    }

    public void disableNotifications() {
        if (userSettings != null) {
            userSettings.setNotificationsEnabled(false);
        }
    }

    public void startThresholdChecking(UserSettings userSettings) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (userSettings != null && userSettings.isNotificationsEnabled()) {
                    double currentPnL = 2000; /* Obtain current P&L for the user */
                    if (currentPnL >= userSettings.getProfitThreshold()) {
                        Notification notification = new Notification("Profit Threshold Crossed",
                                "Your P&L has crossed the profit threshold.");
                        emailSender.sendEmail(userSettings.getEmail(), notification);
                        timer.cancel();
                    } else if (currentPnL <= userSettings.getLossThreshold()) {
                        Notification notification = new Notification("Loss Threshold Crossed",
                                "Your P&L has crossed the loss threshold.");
                        emailSender.sendEmail(userSettings.getEmail(), notification);
                        timer.cancel();
                    }
                }
            }
        }, 0, 60 * 1000); // Check thresholds every 60 seconds
    }
}
