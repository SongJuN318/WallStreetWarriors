package com.example.registration_login_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {
    private String email;
    private double profitThreshold;
    private double lossThreshold;
    private boolean notificationsEnabled;

    public UserSettings(String email) {
        this.email = email;
        profitThreshold = 1000.0; // default profitThreshold
        lossThreshold = -500.0; // default lossThreshold
        notificationsEnabled = true;
    }
}