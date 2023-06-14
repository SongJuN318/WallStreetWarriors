package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.dto.BuyPendingOrderDTO;
import com.example.registration_login_demo.service.BuyService;
import java.security.Principal;
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
    private final AuthController authController;

    public BuyController(BuyService buyService, AuthController authController) {
        this.buyService = buyService;
        this.authController = authController;
    }

    @GetMapping("/{symbol}")
    public String showBuyPage(@PathVariable String symbol) {
        return "buy";
    }

    @PostMapping("/{symbol}")
    public ResponseEntity<Map<String, Object>> executeBuyOrder(
            @RequestBody BuyPendingOrderDTO buyPendingOrderDTO,
            @PathVariable String symbol,
            Principal principal
    ) {
        // Rest of the code
        ResponseEntity<String> executionResult = buyService.executeBuyOrder(buyPendingOrderDTO);
        Map<String, Object> response = new HashMap<>();

        if (executionResult.getStatusCode().is2xxSuccessful()) {
            response.put("success", true);
            response.put("message", executionResult.getBody());
        } else {
            response.put("success", false);
            response.put("message", executionResult.getBody());
        }

        return ResponseEntity.ok().body(response);
    }
}
