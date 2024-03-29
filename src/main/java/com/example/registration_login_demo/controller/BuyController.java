package com.example.registration_login_demo.controller;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.registration_login_demo.dto.BuyPendingOrderDTO;
import com.example.registration_login_demo.service.BuyService;
import com.example.registration_login_demo.service.UserService;

@Controller

public class BuyController {

    private final BuyService buyService;
    private UserService userService;
    private AuthController authController;

    @Autowired
    public BuyController(BuyService buyService, UserService userService, AuthController authController) {
        this.buyService = buyService;
        this.userService = userService;
        this.authController = authController;
    }

    @GetMapping("/buy/{symbol}")
    public String showBuyPage(@PathVariable String symbol, Model model) {
        model.addAttribute("symbol", symbol);
        Optional<String> buyValue = buyService.fetchBuyValue(symbol);
        double buyPrice = 0;
        if (buyValue.isPresent())
            buyPrice = Double.parseDouble(buyValue.get());
        model.addAttribute("marketPrice", buyPrice);
        BuyPendingOrderDTO buyPendingOrderDTO = new BuyPendingOrderDTO();
        model.addAttribute("buyStock", buyPendingOrderDTO);
        return "buy";
    }

    @PostMapping("/buy/{symbol}/save")
    public String executeBuyOrder(@ModelAttribute("buyStock") @RequestBody BuyPendingOrderDTO buyPendingOrderDTO,
            @PathVariable String symbol, Principal principal, Model model) {
        buyPendingOrderDTO.setSymbol(symbol);
        long currentUserId = authController.currentUserId(principal);
        buyPendingOrderDTO.setUserId(currentUserId);
        model.addAttribute("buyStock", buyPendingOrderDTO);
        ResponseEntity<String> responseEntity = buyService.executeBuyOrder(buyPendingOrderDTO);
        String message = responseEntity.getBody();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("successMessage", responseEntity.getBody());
            return "redirect:/buy/{symbol}?success";
        } else {
            if (message.equalsIgnoreCase("A"))
                return "redirect:/buy/{symbol}?marketClosed";
            else if (message.equalsIgnoreCase("B")) {
                return "redirect:/buy/{symbol}?insufficientFunds";
            } else
                return "redirect:/buy/{symbol}?notInRange";
        }
    }
}