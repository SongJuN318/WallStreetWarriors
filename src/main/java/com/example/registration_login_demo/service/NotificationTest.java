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

public class NotificationTest {

    public static void main(String[] args) throws SchedulerException {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();

        JobDetail jobDetail = JobBuilder.newJob(ThresholdJob.class)
                .withIdentity("thresholdJob", "group1")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("thresholdTrigger", "group1")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(10))
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

    public static class ThresholdJob implements Job {

        public ThresholdJob() {
            // Constructor implementation
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            // Job execution logic
            System.out.println("ThresholdJob executed.");
        }
    }
}
