package com.example.convenientparking.Controllers.Admin;

import com.example.convenientparking.Entities.Vehicle;
import com.example.convenientparking.Services.VehicleService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/vehicle-statistic")
public class VehicleStatisticController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/all-vehicles")
    public String showUserVehicles(Model model, Principal principal) {
        List<Vehicle> allVehicles = vehicleService.getallVehicles();
        List<Vehicle> activeVehicles = vehicleService.getVehiclesWithActiveContracts();
        model.addAttribute("allVehicles", allVehicles);
        model.addAttribute("activeVehicles", activeVehicles);
        return "Employee/Vehicle/listVehicle";
    }
}
