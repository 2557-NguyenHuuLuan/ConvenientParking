package com.example.convenientparking.Controllers;

import com.example.convenientparking.Services.MailService;
import com.example.convenientparking.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
@RequestMapping("/password")
public class PasswordResetController {

    @Autowired
    private UserService userService; // Giả định bạn có UserService để quản lý người dùng
    @Autowired
    private MailService mailService;

    private String generatedPin;

    private String mailIsSended;

    // Hiển thị form quên mật khẩu
    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "user/forgotPassword";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        if (userService.isEmailRegistered(email)) {
            generatedPin = String.valueOf(new Random().nextInt(900000) + 100000);
            mailIsSended = email;
            mailService.sendResetPasswordPin(email, generatedPin);
            model.addAttribute("message", "Mã pin đã được gửi cho bạn.");
            model.addAttribute("pinValid", false);
            return "user/verifyPin";
        } else {
            model.addAttribute("message", "Email này chưa được đăng ký.");
            return "user/forgotPassword";
        }
    }

    @PostMapping("/verify-pin")
    public String verifyPin(@RequestParam("pin") String pin, Model model) {
        if (generatedPin != null && generatedPin.equals(pin)) {
            model.addAttribute("pinValid", true);
            return "user/resetPassword";
        } else {
            model.addAttribute("pinValid", false);
            return "user/verifyPin";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("newPassword") String newPassword, Model model) {
        userService.updatePassword(mailIsSended, newPassword);
        generatedPin = null;
        mailIsSended = null;
        model.addAttribute("message", "Your password has been reset successfully.");
        return "user/login";
    }
}