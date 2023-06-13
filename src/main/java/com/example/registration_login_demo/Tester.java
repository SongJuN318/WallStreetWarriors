/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.example.registration_login_demo;

import java.io.IOException;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author User
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(fetchSellValue("7315"));
    }

    public static Optional<String> fetchSellValue(String symbol) {
        try {
            String url = "https://www.bursamalaysia.com/trade/trading_resources/listing_directory/company-profile?stock_code=" + symbol;
            Document doc = Jsoup.connect(url).get();

            Element sellThElement = doc.selectFirst("th:containsOwn(Sell)");
            Element sellValueElement = sellThElement.parent().selectFirst("td");
            String sellValue = sellValueElement.text();

            if (!sellValue.isEmpty()) {
                return Optional.of(sellValue);
            } else {
                System.out.println("Failed to fetch the Sell value for symbol: " + symbol);
                return Optional.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
