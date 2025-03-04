package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByUser(User user);
    boolean existsByNumberPlate(String numberPlate);
    Optional<Vehicle> findById(Long id);
    @Query("SELECT DISTINCT v FROM Vehicle v JOIN v.contractDetails cd WHERE cd.status = true")
    List<Vehicle> findVehiclesWithActiveContracts();
}
