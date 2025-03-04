package com.example.convenientparking.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rental_form")
public class RentalForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String form;

    private Long pricePer;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "rentalForm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalPackage> rentalPackages;
}
