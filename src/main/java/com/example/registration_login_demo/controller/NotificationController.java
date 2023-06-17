package com.example.registration_login_demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.registration_login_demo.service.NotificationService;
import com.example.registration_login_demo.service.ThresholdRequest;

// Import statements

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/enable")
    public ResponseEntity<String> enableNotifications() {
        notificationService.enableNotifications();
        return ResponseEntity.ok("Notifications enabled.");
    }

    @PostMapping("/disable")
    public ResponseEntity<String> disableNotifications() {
        notificationService.disableNotifications();
        return ResponseEntity.ok("Notifications disabled.");
    }

    @PostMapping("/setProfitThreshold")
    public ResponseEntity<String> setProfitThreshold(@RequestBody ThresholdRequest request) {
        double profitThreshold = request.getProfitThreshold();
        notificationService.setProfitThreshold(profitThreshold);
        return ResponseEntity.ok("Profit threshold set successfully.");
    }

    @PostMapping("/setLossThreshold")
    public ResponseEntity<String> setLossThreshold(@RequestBody ThresholdRequest request) {
        double lossThreshold = request.getLossThreshold();
        notificationService.setLossThreshold(lossThreshold);
        return ResponseEntity.ok("Loss threshold set successfully.");
    }
}
