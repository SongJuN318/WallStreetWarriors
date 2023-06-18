package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.*;
import com.example.registration_login_demo.service.BuyService;
import com.example.registration_login_demo.service.NotificationService;
import com.example.registration_login_demo.service.SellService;
import com.example.registration_login_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class DashboardController {
    private final BuyService buyService;
    private final SellService sellService;
    private final UserService userService;
    private AuthController authController;

    @Autowired
    public DashboardController(BuyService buyService,
                               UserService userService,
                               AuthController authController,
                               SellService sellService) {
        this.buyService = buyService;
        this.authController = authController;
        this.userService = userService;
        this.sellService = sellService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        long currentUserId = authController.currentUserId(principal);
        String currentUsername = authController.currentUserName(principal);
        List<TradingHistoryDto> stockByUser = buyService.findHistoryByUserId(currentUserId);
        List<SellDto> sellDtoList = sellService.findSellsByUserId(currentUserId);
        List<BuyPendingOrderDTO> buyPendingOrderByUser = buyService.findBuysPendingByUserId(currentUserId);
        List<SellPendingOrderDTO> sellPendingOrderByUser = sellService.findSellsPendingByUserId(currentUserId);
        double funds = buyService.findBuyUserById(currentUserId).getCurrentFund();
        double pnl = buyService.findBuyUserById(currentUserId).getPnl();
        double points = buyService.findBuyUserById(currentUserId).getPoint();
        model.addAttribute("funds", funds);
        model.addAttribute("pnl", pnl);
        model.addAttribute("points", points);
        model.addAttribute("username", currentUsername);
        model.addAttribute("Buystocks", stockByUser);
        model.addAttribute("Sellstocks", sellDtoList);
        model.addAttribute("BuyPendingStocks", buyPendingOrderByUser);
        model.addAttribute("SellPendingStocks", sellPendingOrderByUser);
        String recipientEmail = principal.getName();
        NotificationService notificationService = new NotificationService(recipientEmail);
        UserSettings userSettings = new UserSettings(recipientEmail);
        notificationService.startThresholdChecking(userSettings, pnl);
        return "dashboard";
    }
}


