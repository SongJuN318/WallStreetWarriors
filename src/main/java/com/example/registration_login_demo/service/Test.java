package com.example.registration_login_demo.service;

import com.example.registration_login_demo.dto.UserSettings;

public class Test {

    public static void main(String[] args) {
        NotificationService2 notificationService = new NotificationService2();

        UserSettings userSettings = new UserSettings("kahchunlim885@gmail.com");

        notificationService.startThresholdChecking(userSettings);

        try {
            Thread.sleep(5 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}