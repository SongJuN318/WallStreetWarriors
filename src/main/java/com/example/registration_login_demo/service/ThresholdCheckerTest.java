// package com.example.registration_login_demo.service;

// import org.quartz.SchedulerException;

// import com.example.registration_login_demo.dto.UserSettings;

// public class ThresholdCheckerTest {
//     public static void main(String[] args) {
//         // Create user settings
//         UserSettings userSettings = new UserSettings();
//         userSettings.setThreshold("AAPL", 2000); // Set a threshold for a symbol (e.g., "AAPL")

//         // Create email sender
//         EmailSender emailSender = new EmailSender();

//         try {
//             // Create threshold checker
//             ThresholdChecker thresholdChecker = new ThresholdChecker(userSettings, emailSender);

//             // Start the threshold checker
//             thresholdChecker.start();

//             // Wait for a while to allow the job to execute (e.g., 5 minutes)
//             Thread.sleep(5 * 60 * 1000);

//             // Stop the threshold checker
//             thresholdChecker.stop();
//         } catch (SchedulerException | InterruptedException e) {
//             e.printStackTrace();
//         }
//     }
// }
