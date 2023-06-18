package com.example.registration_login_demo.controller;


import com.example.registration_login_demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ReportController {
    private final ReportService reportService;
    private AuthController authController;

    @Autowired
    public ReportController(ReportService reportService, AuthController authController) {
        this.reportService = reportService;
        this.authController = authController;
    }

    @GetMapping("/report")
    public String report() {
        return "report";
    }

    @GetMapping("/report/pdf")
    public String downloadAsPdf(Principal principal) {
        long currentUserId = authController.currentUserId(principal);
        reportService.downloadAsPdf(currentUserId);
        return "redirect:/report?pdf";
    }

    @GetMapping("/report/csv")
    public String downloadAsCsv(Principal principal) {
        long currentUserId = authController.currentUserId(principal);
        reportService.downloadAsCSV(currentUserId);
        return "redirect:/report?csv";
    }

    @GetMapping("/report/txt")
    public String downloadAsTxt(Principal principal) {
        long currentUserId = authController.currentUserId(principal);
        reportService.downloadAsCSV(currentUserId);
        return "redirect:/report?txt";
    }
}
