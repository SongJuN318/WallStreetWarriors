package com.example.registration_login_demo.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Notification {
    private String userEmail;
    private String subject;
    private String message;
    private String recipient;

    public Notification(String userEmail, String subject, String message) {
        this.userEmail = userEmail;
        this.subject = subject;
        this.message = message;
    }
}
