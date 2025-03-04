package com.example.convenientparking.Controllers.Admin;

import com.example.convenientparking.Entities.ContractDetail;
import com.example.convenientparking.Entities.ParkingSpot;
import com.example.convenientparking.Entities.ParkingZone;
import com.example.convenientparking.Services.ParkingService;
import com.example.convenientparking.Services.StatisticsService.SpotStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/spot")
public class SpotController {

    @Autowired
    private SpotStatisticsService spotStatisticsService;
    @Autowired
    private ParkingService parkingService;

    private void populateModelWithStatistics(Model model, List<ParkingSpot> spots) {
        Map<Long, Integer> contractCountMap = spots.stream()
                .collect(Collectors.toMap(
                        ParkingSpot::getId,
                        spot -> spotStatisticsService.countPaidContractDetail(spot.getId())
                ));

        Map<Long, Long> revenueMap = spots.stream()
                .collect(Collectors.toMap(
                        ParkingSpot::getId,
                        spot -> spotStatisticsService.getRevenueOfSpot(spot.getId())
                ));

        model.addAttribute("spots", spots);
        model.addAttribute("contractCounts", contractCountMap);
        model.addAttribute("revenueMap", revenueMap);
        model.addAttribute("zones", parkingService.getAllParkingZones());
        model.addAttribute("parkingSpot", new ParkingSpot());
    }

    @GetMapping("/list-spot")
    public String listSpot(Model model, @RequestParam(required = false) Boolean rented) {
        List<ParkingSpot> spots = Boolean.TRUE.equals(rented)
                ? spotStatisticsService.getAllRentedParkingSpots()
                : spotStatisticsService.getAllSpots();
        populateModelWithStatistics(model, spots);
        return "employee/spot/listSpot";
    }

    @GetMapping("/top-rented")
    public String topRentedSpots(Model model) {
        List<ParkingSpot> topSpots = spotStatisticsService.getTopRentedSpots();
        populateModelWithStatistics(model, topSpots);
        return "employee/spot/listSpot";
    }

    @GetMapping("/least-rented")
    public String leastRentedSpots(Model model) {
        List<ParkingSpot> leastSpots = spotStatisticsService.getLeastRentedSpots();
        populateModelWithStatistics(model, leastSpots);
        return "employee/spot/listSpot";
    }

    @GetMapping("/search")
    public String searchSpots(@RequestParam("keyword") String keyword, Model model) {
        List<ParkingSpot> searchResults = spotStatisticsService.searchParkingSpots(keyword);
        populateModelWithStatistics(model, searchResults);
        return "employee/spot/listSpot";
    }

    @GetMapping("/details")
    public String viewContractDetailsBySpotId(@RequestParam("spotId") Long spotId, Model model) {
        List<ContractDetail> contractDetails = spotStatisticsService.getAllContractDetailsBySpotId(spotId);
        model.addAttribute("contractDetails", contractDetails);
        model.addAttribute("spotId", spotId);
        return "employee/spot/contractDetailsOfSpot";
    }

    @PostMapping("/add-parking-spot")
    public String addParkingSpot(@RequestParam Long zoneId, ParkingSpot parkingSpot, Model model) {
        ParkingZone parkingZone = parkingService.getParkingZoneById(zoneId);

        if (parkingZone != null) {
            parkingSpot.setParkingZone(parkingZone);
            parkingSpot.setStatus(true);

            try {
                parkingService.addParkingSpotToZone(parkingZone.getName(), parkingSpot);
                model.addAttribute("message", "Parking Spot added successfully.");
            } catch (IllegalArgumentException e) {
                model.addAttribute("errorMessage", e.getMessage());
            }
        } else {
            model.addAttribute("message", "Failed to add Parking Spot.");
        }
        return "redirect:/spot/list-spot";
    }

}
