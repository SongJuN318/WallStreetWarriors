package com.example.registration_login_demo.controller;

import com.example.registration_login_demo.entity.BuyUser;
import com.example.registration_login_demo.service.BuyService;
import com.example.registration_login_demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LeaderboardController {

    private final BuyService buyService;
    private UserService userService;
    private AuthController authController;

    @Autowired
    public LeaderboardController(BuyService buyService, UserService userService, AuthController authController) {
        this.buyService = buyService;
        this.userService = userService;
        this.authController = authController;
    }

    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        List<BuyUser> topUsers = buyService.getTopUsersByPoints(10);
        List<String> usernames = userService.getUsernamesForBuyUsers(topUsers);
        model.addAttribute("usernames", usernames);
        model.addAttribute("users", topUsers);
        return "leaderboard";
    }
}