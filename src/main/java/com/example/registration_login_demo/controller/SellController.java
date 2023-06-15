package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.BuyDto;
import com.example.registration_login_demo.dto.SellPendingOrderDTO;
import com.example.registration_login_demo.service.BuyService;
import com.example.registration_login_demo.service.SellService;
import com.example.registration_login_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class SellController {
    private final BuyService buyService;
    private UserService userService;
    private AuthController authController;
    private SellService sellService;

    @Autowired
    public SellController(BuyService buyService, UserService userService, AuthController authController, SellService sellService) {
        this.buyService = buyService;
        this.authController = authController;
        this.userService = userService;
        this.sellService = sellService;
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
        Optional<String> sellValue = sellService.fetchSellValue(symbol);
        double sellPrice = 0;
        if (sellValue.isPresent())
            sellPrice = Double.parseDouble(sellValue.get());
        model.addAttribute("marketPrice", sellPrice);
        SellPendingOrderDTO sellPendingOrderDTO = new SellPendingOrderDTO();
        model.addAttribute("sellStock", sellPendingOrderDTO);
        return "sell";
    }

    @PostMapping("/sell/{orderId}/save")
    public String executeBuyOrder(@ModelAttribute("sellStock") @RequestBody SellPendingOrderDTO sellPendingOrderDTO,
                                  @PathVariable long orderId, Principal principal, Model model) {
        sellPendingOrderDTO.setOrderId(orderId);
        long currentUserId = authController.currentUserId(principal);
        sellPendingOrderDTO.setUserId(currentUserId);
        String symbol = buyService.findBuyById(orderId).getSymbol();
        sellPendingOrderDTO.setSymbol(symbol);
        double buyPrice = buyService.findBuyById(orderId).getBuyPrice();
        sellPendingOrderDTO.setBuyPrice(buyPrice);
        model.addAttribute("sellStock", sellPendingOrderDTO);
        ResponseEntity<String> responseEntity = sellService.executeSellOrder(sellPendingOrderDTO);
        String message = responseEntity.getBody();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("successMessage", responseEntity.getBody());
            return "redirect:/sell/{orderId}?success";
        } else {
            if (message.equalsIgnoreCase("A"))
                return "redirect:/sell/{orderId}?marketClosed";
            else if (message.equalsIgnoreCase("B")) {
                return "redirect:/sell/{orderId}?insufficientLots";
            } else return "redirect:/sell/{orderId}?notInRange";
        }
    }
}
