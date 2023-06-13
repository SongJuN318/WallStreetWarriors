package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.BuyPendingOrderDTO;
import com.example.registration_login_demo.service.BuyService;
import com.example.registration_login_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/buy")
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

    @GetMapping("/{symbol}")
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

    //    @PostMapping("/{symbol}/save")
//    public String executeBuyOrder(@ModelAttribute("buyStock") @RequestBody BuyPendingOrderDTO buyPendingOrderDTO, @PathVariable String symbol, Principal principal, Model model) {
//        buyPendingOrderDTO.setSymbol(symbol);
//        long currentUserId = authController.currentUserId(principal);
//        buyPendingOrderDTO.setUserId(currentUserId);
//        model.addAttribute("buyStock", buyPendingOrderDTO);
//        boolean isSuccess = buyService.executeBuyOrder(buyPendingOrderDTO);
//        Map<String, Object> response = new HashMap<>();
//        if (isSuccess)
//            return "redirect:/buy/{symbol}?success";
//        else
//            return "redirect:/buy/{symbol}?pending";
//    }
    @PostMapping("/{symbol}/save")
    public String executeBuyOrder(@ModelAttribute("buyStock") @RequestBody BuyPendingOrderDTO buyPendingOrderDTO, @PathVariable String symbol, Principal principal, Model model) {
        buyPendingOrderDTO.setSymbol(symbol);
        long currentUserId = authController.currentUserId(principal);
        buyPendingOrderDTO.setUserId(currentUserId);
        model.addAttribute("buyStock", buyPendingOrderDTO);
        ResponseEntity<String> responseEntity = buyService.executeBuyOrder(buyPendingOrderDTO);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("successMessage", responseEntity.getBody());
            return "redirect:/buy/{symbol}?success";
        } else {
            model.addAttribute("errorMessage", responseEntity.getBody());
            return "redirect:/buy/{symbol}?pending";
        }
    }
}
