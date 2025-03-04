package com.example.convenientparking.Controllers.Admin;

import com.example.convenientparking.Entities.Contract;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Services.StatisticsService.UserStatisticsService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user-statistics")
public class UserStatisticsController {
    @Autowired
    private UserStatisticsService userStatisticsService;

    @GetMapping("/list-customers")
    public String listCustomers(@NotNull Model model) {
        List<User> customers = userStatisticsService.getUsersIsCustomers();
        model.addAttribute("customers", customers);
        return "Employee/Customer/listCustomers";
    }

    @GetMapping("/customer/contracts")
    public String contracts(@RequestParam("user") Long userId, @NotNull Model model) {
        List<Contract> contracts = userStatisticsService.getAllContractsOfUser(userId);
        model.addAttribute("contracts", contracts);
        return "Employee/Customer/contracts";
    }
}
