package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.ContractDetail;
import com.example.convenientparking.Entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractDetailRepository extends JpaRepository<ContractDetail, Long> {
    List<ContractDetail> findByContractId(Long contractId);
    List<ContractDetail> findByParkingSpotId(Long spotId);
    List<ContractDetail> findByVehicle_Id(Long vehicleId);
    @Query("SELECT c FROM ContractDetail c WHERE c.parkingSpot.id = :spotId " +
            "AND c.rentalStart = :date " +
            "AND c.rentalPackage.rentalForm.form= :form")
    List<ContractDetail> findHourlyContractDetailsOfASpot(@Param("spotId") Long spotId,
                                                           @Param("date") LocalDate Date,
                                                          @Param("form")String form);

    @Query("SELECT c FROM ContractDetail c WHERE c.parkingSpot.id = :spotId AND c.contract.paymentStatus = true")
    List<ContractDetail> getPaidContractDetailByParkingSpotId(@Param("spotId") Long spotId);

    @Query("SELECT COUNT(cd) " +
            "FROM ContractDetail cd " +
            "JOIN cd.contract c " +
            "JOIN cd.rentalPackage rp " +
            "JOIN rp.rentalForm rf " +
            "WHERE rf.id = :rentalFormId AND c.paymentStatus = true")
    Long countContractDetailsByRentalFormWithPaidContracts(@Param("rentalFormId") Long rentalFormId);

    @Query("SELECT SUM(cd.price) " +
            "FROM ContractDetail cd " +
            "JOIN cd.contract c " +
            "JOIN cd.rentalPackage rp " +
            "JOIN rp.rentalForm rf " +
            "WHERE rf.id = :rentalFormId AND c.paymentStatus = true")
    Long calculateTotalRevenueForRentalForm(@Param("rentalFormId") Long rentalFormId);

    @Query("SELECT COUNT(cd) FROM ContractDetail cd " +
            "JOIN cd.contract c " +
            "JOIN cd.rentalPackage rp " +
            "WHERE rp.id = :rentalPackageId AND c.paymentStatus = true")
    Long countContractDetailByRentalPackageWithPaidContracts(@Param("rentalPackageId") Long rentalPackageId);
    boolean existsByVehicleId(Long vehicleId);

}
