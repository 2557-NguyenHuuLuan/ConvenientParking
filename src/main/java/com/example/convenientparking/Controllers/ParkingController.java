package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.ParkingSpot;
import com.example.convenientparking.Entities.ParkingZone;
import com.example.convenientparking.Services.ParkingService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping()
    public String getParkingPotsByZone(@NotNull Model model) {
        List<ParkingZone>  parkingZones = parkingService.getAllParkingZones();
        model.addAttribute("parkingZones", parkingZones);
        return "parking/spots";
    }
}
