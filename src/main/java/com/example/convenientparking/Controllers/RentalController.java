package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.*;
import com.example.convenientparking.Services.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rental")
public class RentalController {

    @Autowired
    private RentalPackageService rentalPackageService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private ContractService contractService;

    private LocalDate tempStartDate;
    private LocalTime tempStartTime;
    private RentalPackage selectedRentalPackage;
    private Vehicle selectedVehicle;
    private Long idContractNeedToAddMore;

    @GetMapping("/rental-forms")
    public String getAllRentalForms(@NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);
        List<RentalForm> rentalForms = rentalPackageService.getAllRentalForms();
        model.addAttribute("rentalForms", rentalForms);
        return "contract/rentalforms";
    }

    @PostMapping("/get-rental-packages")
    public String submitRentalForm(@RequestParam("rentalFormId") Long rentalFormId,@NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);
        List<RentalPackage> rentalPackages = rentalPackageService.getRentalPackagesByRentalFormID(rentalFormId);
        model.addAttribute("rentalPackages", rentalPackages);
        return "contract/rentalpackages";
    }

    @PostMapping("/select-time-and-car")
    public String submitRentalPackage(@RequestParam("packageId") Long packageId,@NotNull Model model) {
        RentalPackage rentalPackage = rentalPackageService.getRentalPackageById(packageId);
        Optional<User> user = userService.getUserAuthentication();
        selectedRentalPackage = rentalPackage;
        List<Vehicle> vehicles = vehicleService.findAllByUser(user.get());
        model.addAttribute("user", user);
        model.addAttribute("vehicles", vehicles);
        if ("HOUR".equals(rentalPackage.getRentalForm().getForm())) {
            return "contract/selectDateTime";
        }
        return "contract/selectDate";
    }

    @PostMapping("/select-spot")
    public String selectSpot(@RequestParam("startDate") LocalDate startDate,
                                 @RequestParam("startTime") LocalTime startTime,
                                 @RequestParam("vehicleId") Long vehicleId,
                                @NotNull Model model) {
        if (startTime == null) {
            startTime = LocalTime.MIDNIGHT;
        }
        selectedVehicle = vehicleService.findById(vehicleId);
        tempStartDate = startDate;
        tempStartTime = startTime;
        List<ParkingZone> parkingZones = parkingService.getParkingZoneValid(tempStartDate,tempStartTime,selectedRentalPackage);
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);
        model.addAttribute("parkingZones", parkingZones);
        return "parking/spots";
    }


    @PostMapping("/create-contract")
    public String createContract(@RequestParam("spotId") Long spotId,@NotNull Model model) {
        ParkingSpot parkingSpot =  parkingService.getParkingSpotById(spotId);
        Optional<User> user = userService.getUserAuthentication();
        Contract currentContract;
        if(idContractNeedToAddMore == null) {
            currentContract = contractService.createContract(user.get());
        }else {
            currentContract = contractService.findContractById(idContractNeedToAddMore);
            idContractNeedToAddMore = null;
        }
        contractService.addMoreDetail(currentContract,
                selectedRentalPackage,
                parkingSpot,
                selectedVehicle,
                tempStartDate,
                tempStartTime );
        contractService.contractValue(currentContract.getId());
        tempStartDate = null;
        tempStartTime = null;
        selectedRentalPackage = null;
        selectedVehicle = null;
        return "redirect:/my-contracts/" + currentContract.getId();
    }

    @PostMapping("/add-more-package")
    public String addMorePackage(@RequestParam("contractId") Long contractId) {
        idContractNeedToAddMore = contractId;
        return "redirect:/rental/rental-forms";
    }
}
