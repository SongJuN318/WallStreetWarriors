package com.example.registration_login_demo.service;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.example.registration_login_demo.entity.Notification;
import com.example.registration_login_demo.entity.UserSettings;

public class ThresholdChecker {
    public void startChecking(UserSettings userSettings) {
        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail job = JobBuilder.newJob(ThresholdCheckingJob.class)
                    .withIdentity("thresholdCheckingJob", "group1")
                    .usingJobData("userSettings", userSettings)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("thresholdCheckingTrigger", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(60)) // Execute every 60 seconds
                    .build();

            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void stopChecking() {
        // Stop the scheduler if needed
    }

    public static class ThresholdCheckingJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            UserSettings userSettings = (UserSettings) context.getJobDetail().getJobDataMap().get("userSettings");
            double currentPnL = 123;// Retrieve the current P&L from your trading system

            if (userSettings.isNotificationsEnabled()) {
                if (currentPnL >= userSettings.getProfitThreshold()) {
                    String subject = "Profit Threshold Crossed";
                    String body = "Congratulations! Your P&L has crossed the profit threshold.";
                    Notification notification = new Notification(userSettings.getEmail(), subject, body);
                    EmailSender.sendEmail(notification.getRecipient(), notification.getSubject(),
                            notification.getMessage());
                } else if (currentPnL <= userSettings.getLossThreshold()) {
                    String subject = "Loss Threshold Crossed";
                    String body = "Sorry! Your P&L has crossed the loss threshold.";
                    Notification notification = new Notification(userSettings.getEmail(), subject, body);
                    EmailSender.sendEmail(notification.getRecipient(), notification.getSubject(),
                            notification.getMessage());
                }
            }
        }
    }
}
