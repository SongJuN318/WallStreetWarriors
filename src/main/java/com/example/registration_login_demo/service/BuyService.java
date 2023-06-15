package com.example.registration_login_demo.service;

import com.example.registration_login_demo.dto.BuyPendingOrderDTO;
import com.example.registration_login_demo.entity.Buy;
import com.example.registration_login_demo.entity.BuyPendingOrder;
import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.repository.BuyPendingOrderRepository;
import com.example.registration_login_demo.repository.BuyRepository;
import com.example.registration_login_demo.repository.BuyUserRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BuyService {

    private final BuyPendingOrderRepository buyPendingOrderRepository;
    private final BuyRepository buyRepository;
    private final BuyUserRepository buyUserRepository;

    @Autowired
    public BuyService(
            BuyPendingOrderRepository buyPendingOrderRepository,
            BuyRepository buyRepository,
            BuyUserRepository buyUserRepository) {
        this.buyPendingOrderRepository = buyPendingOrderRepository;
        this.buyRepository = buyRepository;
        this.buyUserRepository = buyUserRepository;
    }

    public ResponseEntity<String> executeBuyOrder(BuyPendingOrderDTO buyPendingOrderDTO) {
        if (!isTradingHours()) {
            System.out.println("Trading is currently closed. Buy order cannot be executed.");
            return ResponseEntity.badRequest().body("A");
        }

        if (buyPendingOrderDTO.getBuyPrice() <= 0 || buyPendingOrderDTO.getLots() <= 0) {
            System.out.println("Buy price or buy lots are invalid");
            return ResponseEntity.badRequest().body("Buy price or buy lots is invalid.");
        }

        BuyPendingOrder buyPendingOrder = new BuyPendingOrder();
        BuyUser user = buyUserRepository.getById(buyPendingOrderDTO.getUserId());
        buyPendingOrder.setUser(user);

        buyPendingOrder.setSymbol(buyPendingOrderDTO.getSymbol());
        buyPendingOrder.setLots(buyPendingOrderDTO.getLots());
        buyPendingOrder.setBuyPrice((float) buyPendingOrderDTO.getBuyPrice());
        buyPendingOrder.setOrderPendingTime(LocalDateTime.now());

        System.out.println("Symbol: " + buyPendingOrderDTO.getSymbol());
        System.out.println("Buy Price: " + buyPendingOrderDTO.getBuyPrice());
        System.out.println("User id: " + buyPendingOrderDTO.getUserId());

        buyPendingOrderRepository.save(buyPendingOrder);

        Optional<String> buyValue = fetchBuyValue(buyPendingOrderDTO.getSymbol());
        if (buyValue.isPresent()) {
            double buyPrice = Double.parseDouble(buyValue.get());
            double acceptableRange = buyPrice * 0.01;
            double lowerLimit = buyPrice - acceptableRange;
            double upperLimit = buyPrice + acceptableRange;
            double userFunds = user.getCurrentFund();
            System.out.println("Current fund of user: " + userFunds);

            if (isBuyPriceWithinRange(buyPendingOrder, lowerLimit, upperLimit)) {
                double totalCost = calculateTotalCost(buyPendingOrder);
                System.out.println("Total cost: " + totalCost);

                if (isSufficientFunds(totalCost, userFunds)) {
                    executeBuyOrder(buyPendingOrder, totalCost, userFunds);
                    return ResponseEntity.ok("Order executed successfully.");
                } else {
                    return ResponseEntity.badRequest().body("B");
                }
            } else {
                return ResponseEntity.badRequest().body("C");
            }
        } else {
            return ResponseEntity.badRequest().body("Failed to fetch the Buy value for symbol: " + buyPendingOrderDTO.getSymbol());
        }
    }

    // Check if the buy price is within the acceptable range.
    private boolean isBuyPriceWithinRange(BuyPendingOrder buyPendingOrder, double lowerLimit, double upperLimit) {
        double buyPrice = buyPendingOrder.getBuyPrice();
        return buyPrice >= lowerLimit && buyPrice <= upperLimit;
    }

    // Calculate the total number of cost needed for buying lots
    private double calculateTotalCost(BuyPendingOrder buyPendingOrder) {
        int pricePerLots = buyPendingOrder.getLots() * 100;
        return buyPendingOrder.getBuyPrice() * pricePerLots;
    }

    // Check if the user has sufficient fund to pay the total cost for buying lots.
    private boolean isSufficientFunds(double totalCost, double userFunds) {
        return totalCost <= userFunds;
    }

    private void executeBuyOrder(BuyPendingOrder buyPendingOrder, double totalCost, double userFunds) {
        Buy executedOrder = createBuyOrder(buyPendingOrder);
        updateFundsAndSaveBuyOrder(executedOrder, totalCost, userFunds, buyPendingOrder.getUser());
        removeExecutedOrderFromPending(buyPendingOrder);
    }

    private Buy createBuyOrder(BuyPendingOrder buyPendingOrder) {
        Buy executedOrder = new Buy();
        executedOrder.setOrderId(buyPendingOrder.getOrderId());
        executedOrder.setUser(buyPendingOrder.getUser());
        executedOrder.setSymbol(buyPendingOrder.getSymbol());
        executedOrder.setLots(buyPendingOrder.getLots());
        executedOrder.setBuyPrice(buyPendingOrder.getBuyPrice());
        return executedOrder;
    }

    private void updateFundsAndSaveBuyOrder(Buy executedOrder, double totalCost, double userFunds, BuyUser user) {
        user.setCurrentFund(userFunds - totalCost);
        buyUserRepository.save(user);
        buyRepository.save(executedOrder);
        System.out.println("Buy order executed successfully.");
    }

    private void removeExecutedOrderFromPending(BuyPendingOrder buyPendingOrder) {
        buyPendingOrderRepository.deleteById(buyPendingOrder.getOrderId());
        System.out.println("Executed order removed from the pending order table successfully.");
    }

    public Optional<String> fetchBuyValue(String symbol) {
        try {
            String url = "https://www.bursamalaysia.com/trade/trading_resources/listing_directory/company-profile?stock_code=" + symbol;
            Document doc = Jsoup.connect(url).get();

            Element buyThElement = doc.selectFirst("th:containsOwn(Buy)");
            Element buyValueElement = buyThElement.parent().nextElementSibling().select("td").first();
            String buyValue = buyValueElement.text();

            if (!buyValue.isEmpty()) {
                return Optional.of(buyValue);
            } else {
                System.out.println("Failed to fetch the Buy value for symbol: " + symbol);
                return Optional.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private boolean isTradingHours() {
        DayOfWeek currentDay = LocalDateTime.now().getDayOfWeek();
        LocalTime currentTime = LocalTime.now();

        boolean isWeekday = currentDay != DayOfWeek.SATURDAY && currentDay != DayOfWeek.SUNDAY;
        boolean isWithinMorningSession = currentTime.isAfter(LocalTime.of(9, 0)) && currentTime.isBefore(LocalTime.of(12, 30));
        boolean isWithinAfternoonSession = currentTime.isAfter(LocalTime.of(14, 30)) && currentTime.isBefore(LocalTime.of(17, 0));

        return isWeekday && (isWithinMorningSession || isWithinAfternoonSession);
    }

   public List<BuyUser> getTopUsersByPoints(int limit) {
    List<BuyUser> allUsers = buyUserRepository.findAll();

    // Skip the first user if present
    if (!allUsers.isEmpty()) {
        allUsers = allUsers.subList(1, allUsers.size());
    }

    // Sort the remaining users by points in descending order
    allUsers.sort(Comparator.comparing(BuyUser::getPoint).reversed());

    // Return the top users up to the specified limit
    return allUsers.subList(0, Math.min(limit, allUsers.size()));
}

    
    
}
