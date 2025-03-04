package com.example.convenientparking.Services.StatisticsService;

import com.example.convenientparking.Entities.RentalForm;
import com.example.convenientparking.Entities.RentalPackage;
import com.example.convenientparking.Repositories.ContractDetailRepository;
import com.example.convenientparking.Repositories.RentalFormRepository;
import com.example.convenientparking.Repositories.RentalPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PackageStatisticsService {

    @Autowired
    private RentalFormRepository rentalFormRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;

    @Autowired
    private RentalPackageRepository RentalPackageRepository;

    public List<RentalForm> getRentalForms() {
        return rentalFormRepository.findAll();
    }
    public List<RentalPackage> getRentalPackages() {
        return RentalPackageRepository.findAll();
    }

    public RentalForm getRentalFormById(Long id) {
        return rentalFormRepository.findById(id).orElse(null);
    }

    public List<Long> countRentalAmount(List<RentalForm> rentalForms) {
        List<Long> countRentalAmount = new ArrayList<>();
        for (RentalForm rentalForm : rentalForms) {
            Long temp = contractDetailRepository.countContractDetailsByRentalFormWithPaidContracts(rentalForm.getId());
            countRentalAmount.add(temp);
        }

        return countRentalAmount;
    }

    public List<Long> revenueForRentalForm(List<RentalForm> rentalForms) {
        List<Long> revenueForRentalForm = new ArrayList<>();
        for (RentalForm rentalForm : rentalForms) {
            Long temp = contractDetailRepository.calculateTotalRevenueForRentalForm(rentalForm.getId());
            revenueForRentalForm.add(temp);
        }
        return revenueForRentalForm;
    }

    public List<Long> countRentalAmountForRentalPackage(List<RentalPackage> rentalPackages) {
        List<Long> countRentalAmountForRentalPackage = new ArrayList<>();
        for (RentalPackage rentalPackage : rentalPackages) {
            Long temp = contractDetailRepository.countContractDetailByRentalPackageWithPaidContracts(rentalPackage.getId());
            countRentalAmountForRentalPackage.add(temp);
        }
        return countRentalAmountForRentalPackage;
    }

}
