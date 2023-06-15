// package com.example.registration_login_demo.service;

// import javax.mail.MessagingException;
// import javax.mail.Session;
// import javax.mail.internet.MimeMessage;

// import org.springframework.stereotype.Service;

// import com.example.registration_login_demo.entity.Notification;

// @Service
// public class NotificationService {
//    private Session javaMailSender;

//    public NotificationService(Session javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }

//    public void sendNotification(Notification notification) {
//        MimeMessage mail = new MimeMessage(javaMailSender);

//        try {
//            mail.setRecipient(javax.mail.Message.RecipientType.TO,
//                    new javax.mail.internet.InternetAddress(notification.getUserEmail()));
//            mail.setSubject(notification.getSubject());
//            mail.setText(notification.getMessage());
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }

//        try (javax.mail.Transport transport = javaMailSender.getTransport("smtp")) {
//            transport.connect();
//            transport.sendMessage(mail, mail.getAllRecipients());
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
// }
