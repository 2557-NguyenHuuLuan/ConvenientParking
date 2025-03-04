package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.RentalForm;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Services.RentalPackageService;
import com.example.convenientparking.Services.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private RentalPackageService rentalPackageService;

    @GetMapping
    public String home(@NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("formHour", rentalPackageService.findRentalFormByForm("HOUR").getPricePer());
        model.addAttribute("formDay", rentalPackageService.findRentalFormByForm("DAY").getPricePer());
        model.addAttribute("formMonth", rentalPackageService.findRentalFormByForm("MONTH").getPricePer());
        model.addAttribute("user", user);
        return "home/home";
    }

    @GetMapping("/layout")
    public String layout(@NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);
        return "home/layout";
    }

    @GetMapping("/my-account")
    public String myAccount(@NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        if(user.isPresent()) {
            Long balance = user.get().getBalance();
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedBalance = formatter.format(balance) + " VND";
            model.addAttribute("formattedBalance", formattedBalance);
        }
        model.addAttribute("user", user);
        return "home/myAccount";
    }

    @GetMapping("/intro")
    public String intro(@NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("formHour", rentalPackageService.findRentalFormByForm("HOUR").getPricePer());
        model.addAttribute("formDay", rentalPackageService.findRentalFormByForm("DAY").getPricePer());
        model.addAttribute("formMonth", rentalPackageService.findRentalFormByForm("MONTH").getPricePer());
        model.addAttribute("user", user);
        return "home/intro";
    }
}
