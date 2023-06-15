package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.BuyDto;
import com.example.registration_login_demo.service.BuyService;
import com.example.registration_login_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class SellController {
    private final BuyService buyService;
    private UserService userService;
    private AuthController authController;

    @Autowired
    public SellController(BuyService buyService, UserService userService, AuthController authController) {
        this.buyService = buyService;
        this.authController = authController;
        this.userService = userService;
    }

    @GetMapping("/sellList")
    public String sellList(Model model, Principal principal) {
        long currentUserId = authController.currentUserId(principal);
        List<BuyDto> stockByUser = buyService.findBuysByUserId(currentUserId);
        model.addAttribute("stocks", stockByUser);
        return "sellList";
    }

    @GetMapping("/sell/{orderId}")
    public String showSellPage(@PathVariable long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        String symbol = buyService.findBuyById(orderId).getSymbol();
        model.addAttribute("symbol", symbol);
        Optional<String> buyValue = buyService.fetchBuyValue(symbol);
        double buyPrice = 0;
        if (buyValue.isPresent())
            buyPrice = Double.parseDouble(buyValue.get());
        model.addAttribute("marketPrice", buyPrice);
        return "sell";
    }
}
