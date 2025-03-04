package com.example.convenientparking.Repositories;

import com.example.convenientparking.Entities.Contract;
import com.example.convenientparking.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByUser(User user);
    @Query("SELECT c FROM Contract c WHERE c.paymentStatus = false AND (c.writtenOn = :cutoffDate OR c.writtenOn < :cutoffDate)")
    List<Contract> findContractOverdate(@Param("cutoffDate") LocalDate cutoffDate);

    @Query("SELECT c FROM Contract c WHERE c.paymentStatus = true AND c.writtenOn BETWEEN :startDate AND :endDate")
    List<Contract> findContractsWithInvoiceBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT c FROM Contract c WHERE c.paymentStatus = true AND MONTH(c.writtenOn) = :month AND YEAR(c.writtenOn) = :year")
    List<Contract> findContractsByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT c FROM Contract c WHERE c.paymentStatus = true AND YEAR(c.writtenOn) = :year")
    List<Contract> findContractsByYear(@Param("year") int year);

    @Query("SELECT c FROM Contract c where c.user.id = :userId")
    List<Contract> findContractsByUser(@Param("userId") Long userId);
}
