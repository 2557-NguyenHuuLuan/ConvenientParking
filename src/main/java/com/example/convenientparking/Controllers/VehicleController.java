package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.Contract;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Entities.Vehicle;
import com.example.convenientparking.Services.UserService;
import com.example.convenientparking.Services.VehicleService;
import com.example.convenientparking.Services.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleTypeService vehicleTypeService;
    @Autowired
    private UserService userService;

    @GetMapping("/my-vehicles")
    public String showUserVehicles(Model model, Principal principal) {
        String username = principal.getName();
        User usertemp = userService.findByUsername(username);
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);

        if (usertemp != null) {
            List<Vehicle> vehicles = vehicleService.findAllByUser(usertemp);
            model.addAttribute("vehicles", vehicles);
        }

        return "vehicle/userVehicles";
    }

    @GetMapping("/add")
    public String showAddVehicleForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        model.addAttribute("vehicleTypes", vehicleTypeService.getAllVehicleTypes());
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);
        return "vehicle/addVehicle"; // Đường dẫn đến template
    }


    @PostMapping("/add")
    public String addVehicle(@ModelAttribute("vehicle") Vehicle vehicle,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             Principal principal,
                             RedirectAttributes redirectAttributes) {
        try {
            if (vehicleService.existsByNumberPlate(vehicle.getNumberPlate())) {
                redirectAttributes.addFlashAttribute("error", "Biển số xe đã tồn tại!"); // Thêm thông báo lỗi
                return "redirect:/vehicles/add"; // Chuyển hướng lại trang thêm xe
            }

            User user = userService.findByUsername(principal.getName());
            vehicle.setUser(user);
            if (!imageFile.isEmpty()) {
                String fileName = saveImage(imageFile);
                vehicle.setImageUrl("/CarImages/" + fileName);
            }
            vehicleService.saveVehicle(vehicle);
            redirectAttributes.addFlashAttribute("message", "Thêm xe thành công!"); // Thêm thông báo
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm xe!"); // Thêm thông báo lỗi
        }
        return "redirect:/vehicles/my-vehicles";
    }

    @GetMapping("/edit/{id}")
    public String showEditVehicleForm(@PathVariable("id") Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        Optional<Vehicle> vehicleOptional = vehicleService.getVehicleById(id);
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);
        if (vehicleOptional.isPresent()) {
            Vehicle vehicle = vehicleOptional.get();

            // Check if the logged-in user owns the vehicle
            String username = principal.getName();
            if (!vehicle.getUser().getUsername().equals(username)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to edit this vehicle.");
                return "redirect:/vehicles/my-vehicles";
            }

            model.addAttribute("vehicle", vehicle);
            model.addAttribute("vehicleTypes", vehicleTypeService.getAllVehicleTypes());
            return "vehicle/editVehicle";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Vehicle not found.");
            return "redirect:/vehicles/my-vehicles";
        }
    }

    // Handle Update Request
    @PostMapping("/edit/{id}")
    public String editVehicle(@PathVariable("id") Long id,
                              @ModelAttribute("vehicle") Vehicle updatedVehicle,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            Optional<Vehicle> vehicleOptional = vehicleService.getVehicleById(id);
            if (vehicleOptional.isPresent()) {
                Vehicle existingVehicle = vehicleOptional.get();

                // Update vehicle details
                existingVehicle.setNumberPlate(updatedVehicle.getNumberPlate());
                existingVehicle.setVehicleType(updatedVehicle.getVehicleType());

                // Update image if provided
                if (!imageFile.isEmpty()) {
                    String fileName = saveImage(imageFile);
                    existingVehicle.setImageUrl("/CarImages/" + fileName);
                }

                vehicleService.saveVehicle(existingVehicle);
                redirectAttributes.addFlashAttribute("successMessage", "Vehicle updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Vehicle not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating vehicle: " + e.getMessage());
        }
        return "redirect:/vehicles/my-vehicles";
    }


    private String saveImage(MultipartFile imageFile) throws IOException {
        String fileName = imageFile.getOriginalFilename();
        String uploadDir = "src/main/resources/static/CarImages";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = imageFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Không thể lưu file: " + fileName, e);
        }
        return fileName;
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.deleteVehicle(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa xe thành công!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/vehicles/my-vehicles";
    }

}

