package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Services.MailService;
import com.example.convenientparking.Services.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;
    @Autowired
    private UserService userService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    // Display the email form
    @GetMapping("/sendMailForm")
    public String showMailForm() {
        return "mail/sendMail";
    }

    @PostMapping("/sendMail")
    public String sendMail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("text") String text,
            Model model) {

        try {
            mailService.sendMail(to, subject, text);
            model.addAttribute("message", "Email sent successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error sending email: " + e.getMessage());
        }

        return "mail/result"; // Redirect to the result page after sending
    }

    @PostMapping("/verify")
    public String verifyAccount(Model model) {
        Optional<User> userOptional = userService.getUserAuthentication();
        if (userOptional.isPresent()) {
            mailService.sendVerificationEmail(userOptional.get().getEmail(), userOptional.get().getId());
            model.addAttribute("message", "A verification email has been sent to your email address.");
            return "redirect:/";  // Redirect to the home page with a message
        } else {
            model.addAttribute("message", "User not found.");
            return "redirect:/login"; // Redirect to the login page if no user is found
        }
    }

    @GetMapping("/verify-account")
    public String verifyAccountWithId(@RequestParam("userId") Long userId, Model model) {
        Optional<User> userOptional = userService.getUserAuthentication();
        if (userOptional.isPresent() && userOptional.get().getId().equals(userId)) {
            boolean success = userService.verifyUser(userId);
            if (success) {
                model.addAttribute("message", "Account verified successfully.");
                return "redirect:/";
            } else {
                model.addAttribute("message", "Verification failed.");
                return "redirect:/login";
            }
        }
        return "redirect:/login";
    }



}
