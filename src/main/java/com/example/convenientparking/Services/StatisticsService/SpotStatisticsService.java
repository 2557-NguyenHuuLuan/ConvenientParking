package com.example.convenientparking.Services.StatisticsService;

import com.example.convenientparking.Entities.ContractDetail;
import com.example.convenientparking.Entities.ParkingSpot;
import com.example.convenientparking.Repositories.ContractDetailRepository;
import com.example.convenientparking.Repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotStatisticsService {
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;

    public List<ParkingSpot> getAllSpots() {
        return parkingSpotRepository.findAll();
    }

    public List<ParkingSpot> getAllRentedParkingSpots(){
        return parkingSpotRepository.getAllRentedParkingSpot();
    }

    public List<ParkingSpot> getTopRentedSpots() {
        return parkingSpotRepository.findTopRentedSpot();
    }

    public List<ParkingSpot> getLeastRentedSpots() {
        return parkingSpotRepository.findLeastRentedSpot();
    }

    public int countPaidContractDetail(Long spotId){
        return parkingSpotRepository.countContractDetailsForSpotWithInvoice(spotId);
    }

    public List<ParkingSpot> searchParkingSpots(String keyword) {
        return parkingSpotRepository.findByKeyword(keyword);
    }

    public List<ContractDetail> getAllContractDetailsBySpotId(Long spotId) {
        return contractDetailRepository.getPaidContractDetailByParkingSpotId(spotId);
    }

    public Long getRevenueOfSpot(Long spotId) {
        List<ContractDetail> contractDetails = contractDetailRepository.getPaidContractDetailByParkingSpotId(spotId);
        Long revenue = 0L;
        for (ContractDetail contractDetail : contractDetails) {
            revenue += contractDetail.getPrice();
        }
        return revenue;
    }
}
