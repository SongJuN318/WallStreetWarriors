package com.example.registration_login_demo;

import com.example.registration_login_demo.dto.Notification;
import com.example.registration_login_demo.service.EmailSender;

public class EmailSenderTest {
    public static void main(String[] args) {
        EmailSender emailSender = new EmailSender();

        Notification notification = new Notification();
        notification.setSubject("HAHAHA");
        notification.setMessage("This is a test email");

        String recipientEmail = "kahchunlim885@gmail.com";

        emailSender.sendEmail(recipientEmail, notification);
    }
}