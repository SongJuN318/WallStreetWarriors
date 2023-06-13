package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.BuyPendingOrderDTO;
import com.example.registration_login_demo.service.BuyService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/buy")
public class BuyController {

    private final BuyService buyService;

    @Autowired
    public BuyController(BuyService buyService) {
        this.buyService = buyService;
    }

    @GetMapping("/{symbol}")
    public ModelAndView showBuyPage(@PathVariable String symbol) {
        ModelAndView modelAndView = new ModelAndView("buy.html");
        modelAndView.addObject("symbol", symbol);
        return modelAndView;
    }

    @PostMapping("/{symbol}")
    public ResponseEntity<Map<String, Object>> executeBuyOrder(@RequestBody BuyPendingOrderDTO buyPendingOrderDTO, @PathVariable String symbol) {
        buyPendingOrderDTO.setSymbol(symbol);
        boolean isSuccess = buyService.executeBuyOrder(buyPendingOrderDTO);
        Map<String, Object> response = new HashMap<>();
        if (isSuccess) {
            response.put("success", true);
            response.put("message", "Buy order executed successfully.");
        } else {
            response.put("success", false);
            response.put("message", "Buy order execution failed.");
        }
        return ResponseEntity.ok().body(response);
    }
}
