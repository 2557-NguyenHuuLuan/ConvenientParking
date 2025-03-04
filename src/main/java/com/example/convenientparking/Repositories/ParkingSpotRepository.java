package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.ParkingSpot;
import com.example.convenientparking.Entities.ParkingZone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    boolean existsByLocation(String location);

    @Query("SELECT DISTINCT ps FROM ParkingSpot ps JOIN ps.contractDetails cd " +
            "WHERE cd.contract.invoice IS NOT NULL")
    List<ParkingSpot> getAllRentedParkingSpot();

    @Query("SELECT ps FROM ParkingSpot ps JOIN ps.contractDetails cd " +
            "WHERE cd.contract.invoice IS NOT NULL GROUP BY ps.id ORDER BY COUNT(cd) DESC")
    List<ParkingSpot> findTopRentedSpot();

    @Query("SELECT ps FROM ParkingSpot ps JOIN ps.contractDetails cd " +
            "WHERE cd.contract.invoice IS NOT NULL GROUP BY ps.id ORDER BY COUNT(cd) ASC")
    List<ParkingSpot> findLeastRentedSpot();

    @Query("SELECT COUNT(cd) FROM ContractDetail cd " +
            "WHERE cd.parkingSpot.id = :spotId AND cd.contract.invoice IS NOT NULL")
    int countContractDetailsForSpotWithInvoice(Long spotId);

    @Query("SELECT p FROM ParkingSpot p WHERE p.location LIKE %:keyword% OR p.location LIKE %:keyword%")
    List<ParkingSpot> findByKeyword(@Param("keyword") String keyword);


}
