package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.StockDto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StockController {

    private List<StockDto> fetchStockData() throws Exception {
        String apiUrl = "https://api.twelvedata.com/stocks?country=Malaysia";
        URL url = new URL(apiUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray data = jsonObject.getAsJsonArray("data");

            List<StockDto> stockList = new ArrayList<>();
            for (JsonElement element : data) {
                JsonObject stockObject = element.getAsJsonObject();
                String symbol = stockObject.get("symbol").getAsString();
                String name = stockObject.get("name").getAsString();

                StockDto stockDTO = new StockDto();
                stockDTO.setSymbol(symbol);
                stockDTO.setName(name);

                stockList.add(stockDTO);
            }

            return stockList;
        } else {
            throw new Exception("Error: " + responseCode);
        }
    }

    @GetMapping("/stocks")
    public String getStockList(Model model) {
        try {
            List<StockDto> stockList = fetchStockData();
            model.addAttribute("stocks", stockList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred");
        }

        return "stock-list";
    }

    @PostMapping("/search")
    public String searchStocks(@RequestParam("symbol") String symbol, Model model) {
        try {
            List<StockDto> stockList = fetchStockData();

            List<StockDto> matchingStocks = new ArrayList<>();
            for (StockDto stock : stockList) {
                if (stock.getSymbol().startsWith(symbol)) {
                    matchingStocks.add(stock);
                }
            }

            model.addAttribute("stocks", matchingStocks);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred");
        }

        return "search";
    }
}


