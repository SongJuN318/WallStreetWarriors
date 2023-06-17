package com.example.registration_login_demo.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserSettings {
    private String email;
    private double profitThreshold;
    private double lossThreshold;
    private boolean notificationsEnabled;

    public UserSettings(String email) {
        this.email = email;
        profitThreshold = 10000.0; // default profitThreshold
        lossThreshold = -5000.0; // default lossThreshold
        notificationsEnabled = true;
    }
}