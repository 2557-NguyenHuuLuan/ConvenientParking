package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.RentalForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalFormRepository extends JpaRepository<RentalForm, Long> {
    RentalForm findById(long id);
    Optional<RentalForm> findByForm(String form);
}
