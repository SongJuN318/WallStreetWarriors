// package com.example.registration_login_demo.service;

// import java.util.Map;

// import org.quartz.Job;
// import org.quartz.JobBuilder;
// import org.quartz.JobDetail;
// import org.quartz.JobExecutionContext;
// import org.quartz.Scheduler;
// import org.quartz.SchedulerException;
// import org.quartz.SimpleScheduleBuilder;
// import org.quartz.Trigger;
// import org.quartz.TriggerBuilder;
// import org.quartz.impl.StdSchedulerFactory;

// import com.example.registration_login_demo.dto.Notification;
// import com.example.registration_login_demo.dto.UserSettings;

// public class ThresholdChecker {
//     private Scheduler scheduler;
//     private static UserSettings userSettings;
//     private static EmailSender emailSender;

//     public ThresholdChecker(UserSettings userSettings, EmailSender emailSender) throws SchedulerException {
//         this.userSettings = userSettings;
//         this.emailSender = emailSender;

//         scheduler = new StdSchedulerFactory().getScheduler();
//     }

//     public void start() throws SchedulerException {
//         scheduler.start();

//         JobDetail job = JobBuilder.newJob(ThresholdJob.class)
//                 .withIdentity("thresholdJob", "group1")
//                 .build();

//         Trigger trigger = TriggerBuilder.newTrigger()
//                 .withIdentity("thresholdTrigger", "group1")
//                 .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(1)) // Check every minute
//                 .build();

//         scheduler.scheduleJob(job, trigger);
//     }

//     public void stop() throws SchedulerException {
//         scheduler.shutdown();
//     }

//     public static class ThresholdJob implements Job {
//         @Override
//         public void execute(JobExecutionContext context) {
//             // Implement the logic to check thresholds and send notifications
//             // based on the user's settings and trading activity

//             // Example:
//             double currentPnL = 1000; // Replace with your own method

//             if (userSettings.isNotificationsEnabled()) {
//                 Map<String, Double> thresholds = userSettings.getThresholds();
//                 for (Map.Entry<String, Double> entry : thresholds.entrySet()) {
//                     String symbol = entry.getKey();
//                     double threshold = entry.getValue();
//                     if (currentPnL >= threshold) {
//                         String subject = "Threshold crossed for " + symbol;
//                         String message = "Current P&L: " + currentPnL;

//                         Notification notification = new Notification(subject, message);
//                         emailSender.sendEmail("kahchunlim885@gmail.com", notification); // Replace with the actual recipient's email address
//                     }
//                 }
//             }
//         }
//     }
// }
