package com.example.registration_login_demo.service;

import java.util.Timer;
import java.util.TimerTask;

import com.example.registration_login_demo.dto.Notification;
import com.example.registration_login_demo.dto.UserSettings;

public class NotificationService {
    private final EmailSender emailSender;
    private final String recipientEmail;
    private final UserSettings userSettings;

    public NotificationService(String recipientEmail) {
        this.emailSender = new EmailSender();
        this.recipientEmail = recipientEmail;
        this.userSettings = new UserSettings(recipientEmail);
    }

    public void enableNotifications() {
        if (userSettings != null) {
            userSettings.setNotificationsEnabled(true);
        }
    }

    public void disableNotifications() {
        if (userSettings != null) {
            userSettings.setNotificationsEnabled(false);
        }
    }

    public void startThresholdChecking(UserSettings userSettings) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (userSettings != null && userSettings.isNotificationsEnabled()) {
                    double currentPnL = 2000; /* Obtain current P&L for the user */
                    if (currentPnL >= userSettings.getProfitThreshold()) {
                        Notification notification = new Notification("Profit Threshold Crossed",
                                "Your P&L has crossed the profit threshold.");
                        emailSender.sendEmail(userSettings.getEmail(), notification);
                        timer.cancel();
                    } else if (currentPnL <= userSettings.getLossThreshold()) {
                        Notification notification = new Notification("Loss Threshold Crossed",
                                "Your P&L has crossed the loss threshold.");
                        emailSender.sendEmail(userSettings.getEmail(), notification);
                        timer.cancel();
                    }
                }
            }
        }, 0, 60 * 1000); // Check thresholds every 60 seconds
    }

    public void sendRegistrationEmail(String recipientEmail, String userName) {
        Notification notification = new Notification("Welcome to Tradewave - Your Gateway to Financial Opportunities!",
                                "Dear" + userName + ",\r\n" + //
                                        "\r\n" + //
                                        "Welcome to Tradewave! We are thrilled to have you join our community of enthusiastic investors and traders. Congratulations on successfully registering and taking the first step towards unlocking a world of financial opportunities.\r\n" + //
                                        "\r\n" + //
                                        "At Tradewave, we are committed to empowering individuals like you with the tools, knowledge, and resources necessary to navigate the exciting realm of the stock market. Whether you are a seasoned investor or just getting started, our platform offers a wealth of features and insights to help you make informed decisions and achieve your financial goals.\r\n" + //
                                        "\r\n" + //
                                        "Here are a few key highlights and benefits you can expect as a valued member of our community:\r\n" + //
                                        "\r\n" + //
                                        "Comprehensive Market Data: Gain access to real-time market data, including stock prices, charts, financial news, and more. Stay informed and make well-informed investment choices.\r\n" + //
                                        "\r\n" + //
                                        "Portfolio Management: Keep track of your investments effortlessly. Our intuitive portfolio management tools allow you to monitor your holdings, analyze performance, and evaluate your overall investment strategy.\r\n" + //
                                        "\r\n" + //
                                        "Research and Analysis: Leverage our extensive research resources to make informed investment decisions. We provide detailed company profiles, analyst reports, and industry insights to help you stay ahead of the game.\r\n" + //
                                        "\r\n" + //
                                        "Trading Tools and Simulators: Practice your trading skills with our virtual trading simulators or use our advanced trading tools to execute trades efficiently. Explore different strategies without risking your capital.\r\n" + //
                                        "\r\n" + //
                                        "Educational Resources: Enhance your financial knowledge with our curated educational resources, including articles, tutorials, webinars, and expert insights. Expand your understanding of the markets and sharpen your investment acumen.\r\n" + //
                                        "\r\n" + //
                                        "Community Engagement: Connect with a vibrant community of investors, traders, and financial experts. Engage in meaningful discussions, share ideas, and learn from others' experiences. Collaborate and grow together.\r\n" + //
                                        "\r\n" + //
                                        "To get started, simply log in to your account using the credentials you provided during registration. Take a moment to familiarize yourself with the platform, explore the various features, and make the most of our resources. Should you have any questions or need assistance along the way, our dedicated support team is here to help.\r\n" + //
                                        "\r\n" + //
                                        "Thank you once again for choosing Tradewave as your trusted partner in your financial journey. We are excited to embark on this journey together and look forward to witnessing your success.\r\n" + //
                                        "\r\n" + //
                                        "Happy investing!\r\n" + //
                                        "\r\n" + //
                                        "Best regards,\r\n" + //
                                        "\r\n" + //
                                        "Tradewave Official\r\n" + //
                                        "tradewaveofficial@gmail.com");
        emailSender.sendEmail(recipientEmail, notification);
    }
}
