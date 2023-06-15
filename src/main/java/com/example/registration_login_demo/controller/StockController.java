package com.example.registration_login_demo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.registration_login_demo.dto.StockDto;
import com.example.registration_login_demo.dto.StockInfoDto;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


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

    @PostMapping("/search")
    public String performSearch(@RequestParam("stockCode") String stockCode, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("stockCode", stockCode);
        return "redirect:/stock/{stockCode}";
    }

    @GetMapping("/stock/{stockCode}")
    public String showStockInfo(@PathVariable("stockCode") String stockCode, Model model) {
        List<StockInfoDto> stockInfoList = getStockInfoDto(stockCode);
        model.addAttribute("stockInfo", stockInfoList);
        model.addAttribute("stockCode", stockCode);
        return "stockInfo";
    }

    private List<StockInfoDto> getStockInfoDto(String stockCode) {
        try {
            String url = "https://www.bursamalaysia.com/trade/trading_resources/listing_directory/company-profile?stock_code="
                    + stockCode;
            Document doc = Jsoup.connect(url).get();

            List<StockInfoDto> StockInfoDtoList = new ArrayList<>();

            Element titleElement = doc.selectFirst("title");
            if (titleElement != null) {
                String title = titleElement.text();
                String[] parts = title.split(" - ");
                if (parts.length >= 2) {
                    String symbol = parts[0];
                    String stockName = extractStockName(parts[1]);
                    StockInfoDtoList.add(new StockInfoDto("Symbol", symbol));
                    StockInfoDtoList.add(new StockInfoDto("Stock Name", stockName));
                }
            }

            Elements stockStatusElements = doc.select("h3.h5.bold.mb-0 > span");
            for (Element element : stockStatusElements) {
                String className = element.className();
                String value = element.nextSibling().toString().trim();

                if (className.equals("nochange")) {
                    StockInfoDtoList.add(new StockInfoDto("Last Done", "⬌ " + value));
                } else if (className.equals("up")) {
                    StockInfoDtoList.add(new StockInfoDto("Last Done", "🠕 " + value));
                } else if (className.equals("down")) {
                    StockInfoDtoList.add(new StockInfoDto("Last Done", "🠗 " + value));
                }
            }

            Elements stockDataElements = doc.select("div.col-md-4 table.table-striped tbody tr");
            for (Element element : stockDataElements) {
                Element labelElement = element.selectFirst("th");
                Element valueElement = element.selectFirst("td");
                if (labelElement != null && valueElement != null) {
                    String label = labelElement.text();
                    String value = valueElement.text();
                    if (!label.equals("Stock Code")) {
                        StockInfoDtoList.add(new StockInfoDto(label, value));
                    }
                }
            }

            return StockInfoDtoList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    private String extractStockName(String title) {
        Pattern pattern = Pattern.compile("(.*)\\s\\(.*\\)");
        Matcher matcher = pattern.matcher(title);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return title;
    }
}