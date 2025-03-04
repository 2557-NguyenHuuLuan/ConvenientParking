package com.example.convenientparking.Controllers.Admin;


import com.example.convenientparking.Entities.*;
import com.example.convenientparking.Services.AdminService;
import com.example.convenientparking.Services.RoleService;
import com.example.convenientparking.Services.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @GetMapping("")
    public String adminBook() {
        return "admin/homeAdmin";
    }

    @GetMapping("/users")
    public String listUsers(@NotNull Model model) {
        List<User> users = userService.listAllUsersExceptCurrent();
        List<Role> roles = roleService.findAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin/users";
    }

    @PostMapping("/updateRole/add")
    public String addUserRole(@RequestParam Long userId, @RequestParam List<Long> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be selected.");
        }
        adminService.addUserRoles(userId, roles);
        return "redirect:/admin/users";
    }

    @PostMapping("/updateRole/remove")
    public String removeUserRole(@RequestParam Long userId, @RequestParam List<Long> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be selected.");
        }
        adminService.removeUserRoles(userId, roles);
        return "redirect:/admin/users";
    }


    @PostMapping("/users/blockUser")
    public String blockUser(@RequestParam Long userId, @RequestParam(required = false) Boolean lockUser) {
        if (lockUser != null && lockUser) {
            adminService.blockUser(userId);
        } else {
            adminService.unblockUser(userId);
        }
        return "redirect:/admin/users";
    }



}
