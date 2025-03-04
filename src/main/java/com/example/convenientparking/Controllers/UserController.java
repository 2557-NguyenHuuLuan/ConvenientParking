package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (userService.isUsernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username đã tồn tại.");
        }
        if (userService.isEmailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email đã tồn tại.");
        }
        if(userService.isPhoneExists(user.getPhone())) {
            bindingResult.rejectValue("phone","error.user","Số điện thoại đã tồn tại.");
        }
        if (bindingResult.hasErrors()) {
            return "user/register";
        }
        userService.save(user);
        userService.setDefaultRole(user.getUsername());
        return "redirect:/login";
    }

}
