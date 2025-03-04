package com.example.convenientparking.Controllers.Admin;

import com.example.convenientparking.Entities.VehicleType;
import com.example.convenientparking.Services.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/vehicle-types")
public class VehicleTypeController {

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @GetMapping
    public String listVehicleTypes(Model model) {
        model.addAttribute("vehicleTypes", vehicleTypeService.getAllVehicleTypes());
        return "Employee/VehicleType/listVehicleTypes";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("vehicleType", new VehicleType());
        return "Employee/VehicleType/addVehicleType";
    }

    @PostMapping("/add")
    public String addVehicleType(@ModelAttribute VehicleType vehicleType) {
        vehicleTypeService.addVehicleType(vehicleType);
        return "redirect:/admin/vehicle-types";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        VehicleType vehicleType = vehicleTypeService.getVehicleTypeById(id);
        if (vehicleType != null) {
            model.addAttribute("vehicleType", vehicleType);
            return "Employee/VehicleType/editVehicleType";
        }
        return "redirect:/admin/vehicle-types";
    }

    @PostMapping("/edit/{id}")
    public String editVehicleType(@PathVariable Long id, @ModelAttribute VehicleType vehicleType) {
        vehicleTypeService.updateVehicleType(id, vehicleType);
        return "redirect:/admin/vehicle-types";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicleType(@PathVariable Long id) {
        vehicleTypeService.deleteVehicleType(id);
        return "redirect:/admin/vehicle-types";
    }
}

