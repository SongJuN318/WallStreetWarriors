package com.example.registration_login_demo.controller;


import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.registration_login_demo.dto.BuyPendingOrderDTO;
import com.example.registration_login_demo.dto.SellDto;
import com.example.registration_login_demo.dto.SellPendingOrderDTO;
import com.example.registration_login_demo.dto.TradingHistoryDto;
import com.example.registration_login_demo.dto.UserSettings;
import com.example.registration_login_demo.service.BuyService;
import com.example.registration_login_demo.service.EmailSenderService;
import com.example.registration_login_demo.service.NotificationService;
import com.example.registration_login_demo.service.SellService;
import com.example.registration_login_demo.service.UserService;

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
        model.addAttribute("BuyPendingstocks", stockByUser);
        model.addAttribute("SellPendingstocks", sellDtoList);
        String recipientEmail = principal.getName();
        UserSettings userSettings = new UserSettings(recipientEmail);
        EmailSenderService emailSender = new EmailSenderService();
        NotificationService notificationService = new NotificationService(emailSender, userSettings);
        notificationService.startThresholdChecking(pnl);
        return "dashboard";
    }

    public double fetchPnl(Principal principal){
        long currentUserId = authController.currentUserId(principal);
        return buyService.findBuyUserById(currentUserId).getPnl();
    }
}