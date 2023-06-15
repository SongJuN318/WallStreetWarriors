package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.BuyDto;
import com.example.registration_login_demo.repository.BuyUserRepository;
import com.example.registration_login_demo.service.BuyService;
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
    private UserService userService;
    private AuthController authController;


    @Autowired
    public DashboardController(BuyService buyService, UserService userService, AuthController authController, BuyUserRepository buyUserRepository) {
        this.buyService = buyService;
        this.authController = authController;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        long currentUserId = authController.currentUserId(principal);
        String currentUsername = authController.currentUserName(principal);
        List<BuyDto> stockByUser = buyService.findBuysByUserId(currentUserId);
        double funds = buyService.findBuyUserById(currentUserId).getCurrentFund();
        double pnl = buyService.findBuyUserById(currentUserId).getPnl();
        double points = buyService.findBuyUserById(currentUserId).getPoint();
        model.addAttribute("funds", funds);
        model.addAttribute("pnl", pnl);
        model.addAttribute("points", points);
        model.addAttribute("username", currentUsername);
        model.addAttribute("stocks", stockByUser);
        return "dashboard";
    }
}
