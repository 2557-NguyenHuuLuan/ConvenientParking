package com.example.convenientparking.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ContractDetail")
public class ContractDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "rentalpackage_id", nullable = false)
    private RentalPackage rentalPackage;

    @ManyToOne
    @JoinColumn(name = "parkingspot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = true)
    private Vehicle vehicle;


    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate rentalStart;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate rentalEnd;
    private LocalTime rentalStartTime;
    private LocalTime rentalEndTime;
    private Long price;
    private boolean status;
}
