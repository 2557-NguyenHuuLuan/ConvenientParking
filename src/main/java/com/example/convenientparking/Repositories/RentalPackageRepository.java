package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.RentalForm;
import com.example.convenientparking.Entities.RentalPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalPackageRepository extends JpaRepository<RentalPackage, Long> {

    List<RentalPackage> findByRentalForm(RentalForm rentalForm);

    boolean existsByRentalFormAndNumberOf(RentalForm rentalForm, Long numberOf);
}
