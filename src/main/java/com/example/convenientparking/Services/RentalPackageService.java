package com.example.convenientparking.Services;

import com.example.convenientparking.Entities.RentalForm;
import com.example.convenientparking.Entities.RentalPackage;
import com.example.convenientparking.Repositories.RentalFormRepository;
import com.example.convenientparking.Repositories.RentalPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalPackageService {
    @Autowired
    private RentalPackageRepository rentalPackageRepository;

    @Autowired
    private RentalFormRepository formRepository;

    public List<RentalForm> getAllRentalForms() {
        return formRepository.findAll();
    }
    public RentalForm findRentalFormByForm(String form) {
        Optional<RentalForm> rentalForm = formRepository.findByForm(form);
        return rentalForm.orElse(null);
    }
    public List<RentalPackage> getRentalPackagesByRentalFormID(Long rentalFormID) {
        RentalForm rentalForm = formRepository.findById(rentalFormID).orElse(null);
        return rentalPackageRepository.findByRentalForm(rentalForm);
    }

    public RentalPackage getRentalPackageById(Long rentalPackageID) {
        return rentalPackageRepository.findById(rentalPackageID).orElse(null);
    }

    public RentalPackage addRentalPackage(Long rentalFormId, RentalPackage rentalPackage) {
        RentalForm rentalForm = formRepository.findById(rentalFormId).orElse(null);
        if (rentalForm == null) {
            return null;
        }

        boolean exists = rentalPackageRepository.existsByRentalFormAndNumberOf(rentalForm, rentalPackage.getNumberOf());
        if (exists) {
            return null;
        }

        rentalPackage.setRentalForm(rentalForm);
        return rentalPackageRepository.save(rentalPackage);
    }

    public RentalForm getRentalFormById(Long id) {
        return formRepository.findById(id).orElse(null);
    }

    // Lưu thông tin RentalForm
    public void saveRentalForm(RentalForm rentalForm) {
        formRepository.save(rentalForm);
    }

}
