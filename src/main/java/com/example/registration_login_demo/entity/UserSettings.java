package com.example.registration_login_demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSettings {
    private boolean notificationsEnabled;
    private double profitThreshold;
    private double lossThreshold;
    private String email;
}