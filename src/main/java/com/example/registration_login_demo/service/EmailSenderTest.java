package com.example.registration_login_demo.service;

import com.example.registration_login_demo.dto.Notification;

public class EmailSenderTest {
    public static void main(String[] args) {
        EmailSender emailSender = new EmailSender();

        // Create a sample notification
        Notification notification = new Notification();
        notification.setSubject("Testing EmailSender");
        notification.setMessage("This is a test email");

        // Set the recipient email address
        String recipientEmail = "kahchunlim885@gmail.com";

        // Send the email
        emailSender.sendEmail(recipientEmail, notification);
    }
}