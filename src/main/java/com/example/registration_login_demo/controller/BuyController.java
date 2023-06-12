package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.BuyPendingOrderDTO;
import com.example.registration_login_demo.service.BuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buy")
public class BuyController {
    private final BuyService buyService;

    
    public BuyController(BuyService buyService) {
        this.buyService = buyService;
    }

    @PostMapping("/{symbol}")
    public ResponseEntity<String> executeBuyOrder(@RequestBody BuyPendingOrderDTO buyPendingOrderDTO, @PathVariable String symbol) {
        buyPendingOrderDTO.setSymbol(symbol);
        if(buyService.executeBuyOrder(buyPendingOrderDTO)) {
            return ResponseEntity.ok("Buy order executed successfully.");
        } else {
            return ResponseEntity.ok("Buy order executed failed.");
        }
        
    }
}
