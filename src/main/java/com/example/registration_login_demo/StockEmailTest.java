package com.example.registration_login_demo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.example.registration_login_demo.dto.Notification;
import com.example.registration_login_demo.dto.UserSettings;
import com.example.registration_login_demo.service.EmailSender;

public class StockEmailTest {
    private final EmailSender emailSender;
    private final Map<String, UserSettings> userSettingsMap;

    public StockEmailTest() {
        this.emailSender = new EmailSender();
        this.userSettingsMap = new HashMap<>();
    }

    public void configureUserSettings(String email, double profitThreshold, double lossThreshold,
            boolean notificationsEnabled) {
        UserSettings userSettings = new UserSettings(email, profitThreshold, lossThreshold, notificationsEnabled);
        userSettingsMap.put(email, userSettings);
    }

    public void enableNotifications(String email) {
        UserSettings userSettings = userSettingsMap.get(email);
        if (userSettings != null) {
            userSettings.setNotificationsEnabled(true);
        }
    }

    public void disableNotifications(String email) {
        UserSettings userSettings = userSettingsMap.get(email);
        if (userSettings != null) {
            userSettings.setNotificationsEnabled(false);
        }
    }

    public void startThresholdChecking() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (UserSettings userSettings : userSettingsMap.values()) {
                    if (userSettings.isNotificationsEnabled()) {
                        double currentPnL = fetchBuyValue("4715");
                        /* Obtain current P&L for the user */;
                        if (currentPnL > 2.550) {
                            Notification notification = new Notification("UP GENTING MALAYSIA BERHAD",
                                    "Buy Price is " + currentPnL);
                            emailSender.sendEmail(userSettings.getEmail(), notification);
                        } else if(currentPnL < 2.550){
                            Notification notification = new Notification("DOWN GENTING MALAYSIA BERHAD",
                                    "Buy Price is " + currentPnL);
                            emailSender.sendEmail(userSettings.getEmail(), notification);
                        } else if (currentPnL == 2.550) {
                            Notification notification = new Notification("NO CHANGE GENTING MALAYSIA BERHAD",
                                    "Buy Price is " + currentPnL);
                            emailSender.sendEmail(userSettings.getEmail(), notification);
                    }
                }
            }
        }, 0, 60 * 1000); // Check thresholds every 60 seconds
    }

    public double fetchBuyValue(String symbol) {
        try {
            String url = "https://www.bursamalaysia.com/trade/trading_resources/listing_directory/company-profile?stock_code="
                    + symbol;
            Document doc = Jsoup.connect(url).get();

            Element buyThElement = doc.selectFirst("th:containsOwn(Buy)");
            Element buyValueElement = buyThElement.parent().nextElementSibling().select("td").first();
            String buyValue = buyValueElement.text();
            double buyPrice = Double.parseDouble(buyValue);
            return buyPrice;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void main(String[] args) {
        StockEmailTest notificationService = new StockEmailTest();

        // Configure user settings
        notificationService.configureUserSettings("kahchunlim885@gmail.com", 2000.0, -500.0, true);

        // Start threshold checking
        notificationService.startThresholdChecking();

        // Sleep for some time to allow the threshold checking to occur (e.g., 5 minutes)
        try {
            Thread.sleep(5 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}