package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.SearchDetailDto;
import com.example.registration_login_demo.dto.StockDto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @GetMapping("/search")
    public String searchStocks(@RequestParam(required = false) String symbol, Model model) {
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

    @GetMapping("/stock/{symbol}")
    public String searchStockDetails(@PathVariable String symbol, Model model) {
        try {
            String url = "https://www.bursamalaysia.com/trade/trading_resources/listing_directory/company-profile?stock_code=" + symbol;
            Document doc = Jsoup.connect(url).get();

            Elements stockDataElements = doc.select("div.col-md-4 table.table-striped tbody tr");

            // Create a SearchDetailDto object
            SearchDetailDto searchDetailDto = new SearchDetailDto();

            int index = 0;
            for (Element element : stockDataElements) {
                Element labelElement = element.selectFirst("th");
                Element valueElement = element.selectFirst("td");

                if (labelElement != null && valueElement != null) {
                    String value = valueElement.text();

                    switch (index) {
                        case 0:
                            searchDetailDto.setStockCode(value);
                            break;
                        case 1:
                            searchDetailDto.setChange(value);
                            break;
                        case 2:
                            searchDetailDto.setPercentageChange(value);
                            break;
                        case 3:
                            searchDetailDto.setVolume(value);
                            break;
                        case 4:
                            searchDetailDto.setBuyVolume(value);
                            break;
                        case 5:
                            searchDetailDto.setBuy(value);
                            break;
                        case 6:
                            searchDetailDto.setSell(value);
                            break;
                        case 7:
                            searchDetailDto.setSellVolume(value);
                            break;
                        case 8:
                            searchDetailDto.setLacp(value);
                            break;
                        case 9:
                            searchDetailDto.setOpen(value);
                            break;
                        case 10:
                            searchDetailDto.setHigh(value);
                            break;
                        case 11:
                            searchDetailDto.setLow(value);
                            break;
                    }
                    index++;
                }
            }

            model.addAttribute("stock", searchDetailDto);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while fetching stock details.");
        }

        return "searchDetail";
    }

}
