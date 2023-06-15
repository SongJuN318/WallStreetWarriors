package com.example.registration_login_demo.service;

import com.example.registration_login_demo.dto.SellPendingOrderDTO;
import com.example.registration_login_demo.entity.Buy;
import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.entity.Sell;
import com.example.registration_login_demo.entity.SellPendingOrder;
import com.example.registration_login_demo.repository.BuyRepository;
import com.example.registration_login_demo.repository.BuyUserRepository;
import com.example.registration_login_demo.repository.SellPendingOrderRepository;
import com.example.registration_login_demo.repository.SellRepository;
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
import java.util.Optional;

@Service
public class SellService {

    private final SellPendingOrderRepository sellPendingOrderRepository;
    private final SellRepository sellRepository;
    private final BuyRepository buyRepository;
    private final BuyUserRepository buyUserRepository;

    @Autowired
    public SellService(
            SellPendingOrderRepository sellPendingOrderRepository,
            SellRepository sellRepository,
            BuyRepository buyRepository,
            BuyUserRepository buyUserRepository) {
        this.sellPendingOrderRepository = sellPendingOrderRepository;
        this.sellRepository = sellRepository;
        this.buyRepository = buyRepository;
        this.buyUserRepository = buyUserRepository;
    }

    public ResponseEntity<String> executeSellOrder(SellPendingOrderDTO sellPendingOrderDTO) {
        if (!isTradingHours()) {
            System.out.println("Trading is currently closed. Sell order cannot be executed.");
            return ResponseEntity.badRequest().body("A");
        }

        if (sellPendingOrderDTO.getSellPrice() <= 0 || sellPendingOrderDTO.getLots() <= 0) {
            System.out.println("Sell price or sell lots are invalid");
            return ResponseEntity.badRequest().body("C");
        }

        SellPendingOrder sellPendingOrder = new SellPendingOrder();
        BuyUser user = buyUserRepository.getById(sellPendingOrderDTO.getUserId());
        sellPendingOrder.setUser(user);
        sellPendingOrder.setBuyPrice(sellPendingOrderDTO.getBuyPrice());
        sellPendingOrder.setOrderId(sellPendingOrderDTO.getOrderId());
        sellPendingOrder.setSymbol(sellPendingOrderDTO.getSymbol());
        sellPendingOrder.setLots(sellPendingOrderDTO.getLots());
        sellPendingOrder.setSellPrice((float) sellPendingOrderDTO.getSellPrice());
        sellPendingOrder.setSellPendingTime(LocalDateTime.now());

        System.out.println("Symbol: " + sellPendingOrderDTO.getSymbol());
        System.out.println("Sell Price: " + sellPendingOrderDTO.getSellPrice());
        System.out.println("User id: " + sellPendingOrderDTO.getUserId());

        sellPendingOrderRepository.save(sellPendingOrder);

        double purchasePrice = getPurchasePriceForIdAndUser(sellPendingOrderDTO.getOrderId(), user);
        if (purchasePrice > 0) {
            double totalCost = calculateTotalCost(purchasePrice, sellPendingOrderDTO.getLots());
            System.out.println("Total cost: " + totalCost);

            double userFunds = user.getCurrentFund();
            if (isSufficientLots(sellPendingOrder.getOrderId(), sellPendingOrderDTO.getLots(), user) ||
                    isEqualLots(sellPendingOrder.getOrderId(), sellPendingOrderDTO.getLots(), user)) {
                executeSellOrder(sellPendingOrder, totalCost, userFunds);
                return ResponseEntity.ok("Sell order executed successfully.");
            } else {
                return ResponseEntity.badRequest().body("B");
            }
        } else {
            return ResponseEntity.badRequest().body("No purchase found for the symbol: " + sellPendingOrderDTO.getSymbol());
        }
    }

    private double getPurchasePriceForIdAndUser(long id, BuyUser user) {
        Buy purchases = buyRepository.findByUserAndOrderId(user, id);
        return purchases.getBuyPrice();
    }

    private double calculateTotalCost(double purchasePrice, int lots) {
        int pricePerLot = lots * 100;
        return purchasePrice * pricePerLot;
    }

    private boolean isSufficientLots(long id, int lots, BuyUser user) {
        Buy purchases = buyRepository.findByUserAndOrderId(user, id);
        int totalLots = purchases.getLots();
        return totalLots > lots;
    }

    private boolean isEqualLots(long id, int lots, BuyUser user) {
        Buy purchases = buyRepository.findByUserAndOrderId(user, id);
        int totalLots = purchases.getLots();
        return totalLots == lots;
    }

    private void executeSellOrder(SellPendingOrder sellPendingOrder, double totalCost, double userFunds) {
        double sellPrice = sellPendingOrder.getSellPrice();
        int lots = sellPendingOrder.getLots();
        double totalSellPrice = calculateTotalSellPrice(sellPrice, lots);
        double profitOrLoss = calculateProfitOrLoss(totalSellPrice, totalCost);

        Sell executedOrder = createSellOrder(sellPendingOrder, profitOrLoss);
        updateFundsAndSaveSellOrder(executedOrder, userFunds + totalSellPrice, sellPendingOrder.getUser());
//        removeExecutedOrderFromBuy(sellPendingOrder.getOrderId(), lots, sellPendingOrder.getUser());
        removeExecutedOrderFromPending(sellPendingOrder);
    }

    private Sell createSellOrder(SellPendingOrder sellPendingOrder, double profitOrLoss) {
        Sell executedOrder = new Sell();
        executedOrder.setUser(sellPendingOrder.getUser());
        executedOrder.setSymbol(sellPendingOrder.getSymbol());
        executedOrder.setLots(sellPendingOrder.getLots());
        executedOrder.setSellPrice(sellPendingOrder.getSellPrice());
        executedOrder.setProfitLost(profitOrLoss);
        return executedOrder;
    }

    private void updateFundsAndSaveSellOrder(Sell executedOrder, double updatedFunds, BuyUser user) {
        user.setCurrentFund(updatedFunds);
        buyUserRepository.save(user);
        sellRepository.save(executedOrder);
        System.out.println("Sell order executed successfully.");
    }

    private void removeExecutedOrderFromBuy(long id, int lots, BuyUser user) {
        Buy purchase = buyRepository.findByUserAndOrderId(user, id);
        if (isEqualLots(id, lots, user))
            buyRepository.delete(purchase);
        else if (isSufficientLots(id, lots, user)) {
            purchase.setLots(purchase.getLots() - lots);
            buyRepository.save(purchase);
        }
        System.out.println("Executed order removed from the buy order table successfully.");
    }

    private void removeExecutedOrderFromPending(SellPendingOrder sellPendingOrder) {
        sellPendingOrderRepository.delete(sellPendingOrder);
        System.out.println("Executed order removed from the pending order table successfully.");
    }

    private double calculateTotalSellPrice(double sellPrice, int lots) {
        int pricePerLot = lots * 100;
        return sellPrice * pricePerLot;
    }

    private double calculateProfitOrLoss(double totalSellPrice, double totalCost) {
        return totalSellPrice - totalCost;
    }

    private boolean isTradingHours() {
        DayOfWeek currentDay = LocalDateTime.now().getDayOfWeek();
        LocalTime currentTime = LocalTime.now();

        boolean isWeekday = currentDay != DayOfWeek.SATURDAY && currentDay != DayOfWeek.SUNDAY;
        boolean isWithinMorningSession = currentTime.isAfter(LocalTime.of(9, 0)) && currentTime.isBefore(LocalTime.of(12, 30));
        boolean isWithinAfternoonSession = currentTime.isAfter(LocalTime.of(14, 30)) && currentTime.isBefore(LocalTime.of(17, 0));

        return isWeekday && (isWithinMorningSession || isWithinAfternoonSession);
    }

    public Optional<String> fetchSellValue(String symbol) {
        try {
            String url = "https://www.bursamalaysia.com/trade/trading_resources/listing_directory/company-profile?stock_code=" + symbol;
            Document doc = Jsoup.connect(url).get();

            Element sellThElement = doc.selectFirst("th:containsOwn(Sell)");
            Element sellValueElement = sellThElement.parent().select("td").first();
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
