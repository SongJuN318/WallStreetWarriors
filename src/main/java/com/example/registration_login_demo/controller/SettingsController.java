// package com.example.registration_login_demo.controller;

// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.registration_login_demo.service.NotificationService;

// @RestController
// public class SettingsController {
//     private final NotificationService notificationService;

//     public SettingsController(NotificationService notificationService) {
//         this.notificationService = notificationService;
//     }

//     @PostMapping("/enableNotifications")
//     public void enableNotifications() {
//         notificationService.enableNotifications();
//     }

//     @PostMapping("/disableNotifications")
//     public void disableNotifications() {
//         notificationService.disableNotifications();
//     }

// //     @PostMapping("/setThreshold")
// //     public void setThreshold(@RequestBody ThresholdRequest request) {
// //         double profitThreshold = request.getProfitThreshold();
// //         // Update the user settings with the new threshold
// //         notificationService.getUserSettings().setProfitThreshold(profitThreshold);
// //     }
// // }

// // class ThresholdRequest {
    
// // }
// }