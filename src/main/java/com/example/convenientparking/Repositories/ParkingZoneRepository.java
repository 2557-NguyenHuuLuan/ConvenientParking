package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.ParkingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingZoneRepository extends JpaRepository<ParkingZone, Long> {
    Optional<ParkingZone> findByName(String name);
}