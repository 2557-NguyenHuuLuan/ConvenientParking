package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Services.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class PaymentController {
    @Autowired
    private UserService userService;

    @GetMapping("/deposit")
    public String showDepositPage(@NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);
        return "payment/deposit"; // Trả về tên của file deposit.html trong thư mục templates
    }
    @GetMapping("/success")
    public String success() {
        return "payment/success"; // Trả về tên của tệp HTML success.html
    }
    @GetMapping("/failed")
    public String showErrorPage() {
        return "payment/failed"; // Trả về trang lỗi error.html
    }
}