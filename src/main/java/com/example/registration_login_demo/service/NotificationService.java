package com.example.registration_login_demo.service;

import java.security.Principal;
import java.util.Timer;
import java.util.TimerTask;

import com.example.registration_login_demo.dto.Notification;
import com.example.registration_login_demo.dto.UserSettings;

public class NotificationService {
    private final EmailSender emailSender;
    private UserSettings userSettings;

    public NotificationService() {
        this.emailSender = new EmailSender();
    }

    public void configureUserSettings(String email, double profitThreshold, double lossThreshold,
            boolean notificationsEnabled) {
        this.userSettings = new UserSettings(email, profitThreshold, lossThreshold, notificationsEnabled);
    }

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

    public void startThresholdChecking() {
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

    public String getEmail(Principal principal) {
        return principal.getName();
    }
}
