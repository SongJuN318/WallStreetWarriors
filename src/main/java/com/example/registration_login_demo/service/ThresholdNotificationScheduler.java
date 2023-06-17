package com.example.registration_login_demo.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.registration_login_demo.dto.NotificationDto;
import com.example.registration_login_demo.dto.UserSettings;

public class ThresholdNotificationScheduler {
    private final EmailSenderService emailSender;
    private final UserSettings userSettings;

    public ThresholdNotificationScheduler(UserSettings userSettings) {
        this.emailSender = new EmailSenderService();
        this.userSettings = userSettings;
    }

    public void startThresholdChecking(double currentPnL) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (userSettings.isNotificationsEnabled()) {
                    if (currentPnL >= userSettings.getProfitThreshold()) {
                        NotificationDto notification = new NotificationDto("Profit Threshold Crossed",
                                "Your P&L has crossed the profit threshold.");
                        emailSender.sendEmail(userSettings.getEmail(), notification);
                        timer.cancel();
                    } else if (currentPnL <= userSettings.getLossThreshold()) {
                        NotificationDto notification = new NotificationDto("Loss Threshold Crossed",
                                "Your P&L has crossed the loss threshold.");
                        emailSender.sendEmail(userSettings.getEmail(), notification);
                        timer.cancel();
                    }
                }
            }
        }, 0, 60 * 1000); // Check thresholds every 60 seconds
    }
}
