package com.example.registration_login_demo.service;

public class Test {

    public static void main(String[] args) {
        NotificationService notificationService = new NotificationService();

        // Configure user settings
        notificationService.configureUserSettings("kahchunlim885@gmail.com", 2000.0, -500.0, true);

        // Start threshold checking
        notificationService.startThresholdChecking();

        // Sleep for some time to allow the threshold checking to occur (e.g., 5 minutes)
        try {
            Thread.sleep(5 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Configure new user settings
        notificationService.configureUserSettings("anotheruser@example.com", 1500.0, -700.0, true);

        // Start threshold checking again
        notificationService.startThresholdChecking();

        // Sleep for some time
        try {
            Thread.sleep(5 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop the threshold checking (optional)
        // timer.cancel();

        // Verify that the email notifications were sent

        // Check the recipient's email inbox for received emails
        // Verify that the correct notifications were sent based on the configured thresholds
    }
}

