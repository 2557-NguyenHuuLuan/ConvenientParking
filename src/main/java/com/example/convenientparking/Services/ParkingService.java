package com.example.convenientparking.Services;

import com.example.convenientparking.Entities.ContractDetail;
import com.example.convenientparking.Entities.ParkingSpot;
import com.example.convenientparking.Entities.ParkingZone;
import com.example.convenientparking.Entities.RentalPackage;
import com.example.convenientparking.Repositories.ContractDetailRepository;
import com.example.convenientparking.Repositories.ParkingSpotRepository;
import com.example.convenientparking.Repositories.ParkingZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ParkingService {
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    @Autowired
    private ParkingZoneRepository parkingZoneRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;

    public List<ParkingZone> getAllParkingZones(){
        return  parkingZoneRepository.findAll();
    }

    public ParkingSpot getParkingSpotById(Long parkingSpotId) {
        return parkingSpotRepository.findById(parkingSpotId).get();
    }

    public ParkingZone getParkingZoneById(Long id) {
        return parkingZoneRepository.findById(id).orElse(null);
    }
    public ParkingZone getParkingZoneByName(String zoneName) {
        return parkingZoneRepository.findByName(zoneName).get();
    }
    public boolean isLocationUnique(String location) {
        return !parkingSpotRepository.existsByLocation(location);
    }

    public ParkingSpot addParkingSpotToZone(String zoneName, ParkingSpot parkingSpot) {
        ParkingZone parkingZone = getParkingZoneByName(zoneName);
        if (parkingZone != null) {
            parkingSpot.setParkingZone(parkingZone);
            if (!isLocationUnique(parkingSpot.getLocation())) {
                throw new IllegalArgumentException("Location " + parkingSpot.getLocation() + " already exists.");
            }
            return parkingSpotRepository.save(parkingSpot);
        }
        return null; // Or throw an exception if zone is not found
    }

    public List<ParkingZone> getParkingZoneValid(LocalDate tempStartDate,
                                                 LocalTime tempStartTime,
                                                 RentalPackage selectedRentalPackage) {
        List<ParkingZone> zonesValid = parkingZoneRepository.findAll();
        for (ParkingZone zone : zonesValid) {
            for (ParkingSpot spot : zone.getParkingSpots()) {
                if("HOUR".equals(selectedRentalPackage.getRentalForm().getForm())){
                    spot.setStatus(chechHour(tempStartDate, tempStartTime,spot,selectedRentalPackage));
                }else{
                    spot.setStatus(checkDate(tempStartDate,spot,selectedRentalPackage));
                }
            }
        }
        return zonesValid;
    }

    public boolean chechHour(LocalDate tempStartDate,
                             LocalTime tempStartTime,
                             ParkingSpot spot,
                             RentalPackage selectedRentalPackage){
        boolean result = true;
        List<ContractDetail> hourlyContractDetails = contractDetailRepository.findHourlyContractDetailsOfASpot(spot.getId(),tempStartDate, "HOUR");
        LocalTime tempEndTime = tempStartTime.plusHours(selectedRentalPackage.getNumberOf());
        for(ContractDetail contractDetail : hourlyContractDetails){
            if(contractDetail.getContract().isStatus() == true){
                if(tempStartTime.equals(contractDetail.getRentalStartTime())){
                    result = false;
                } else if(tempStartTime.isBefore(contractDetail.getRentalStartTime()) && tempEndTime.isAfter(contractDetail.getRentalStartTime())){
                    result = false;
                }else if(tempStartTime.isAfter(contractDetail.getRentalStartTime()) && tempStartTime.isBefore(contractDetail.getRentalEndTime())){
                    result = false;
                }
            }

        }
        return result;
    }

    public boolean checkDate(LocalDate tempStartDate,
                             ParkingSpot spot,
                             RentalPackage selectedRentalPackage){
        boolean result = true;
        LocalDate tempEndDate;
        if("DAY".equals(selectedRentalPackage.getRentalForm().getForm())){
            tempEndDate = tempStartDate.plusDays(selectedRentalPackage.getNumberOf());
        } else if("MONTH".equals(selectedRentalPackage.getRentalForm().getForm())){
            tempEndDate = tempStartDate.plusMonths(selectedRentalPackage.getNumberOf());
        }else{
            tempEndDate = tempStartDate.plusYears(selectedRentalPackage.getNumberOf());
        }
        List<ContractDetail> contractDetails = contractDetailRepository.findByParkingSpotId(spot.getId());
        for(ContractDetail contractDetail : contractDetails){
            if(contractDetail.getContract().isStatus() == true){
                if(tempStartDate.equals(contractDetail.getRentalStart())){
                    result = false;
                } else if(tempStartDate.isBefore(contractDetail.getRentalStart()) && tempEndDate.isAfter(contractDetail.getRentalStart())){
                    result = false;
                }else if(tempStartDate.isAfter(contractDetail.getRentalStart()) && tempStartDate.isBefore(contractDetail.getRentalEnd())){
                    result = false;
                }
            }
        }
        return result;

    }
}
