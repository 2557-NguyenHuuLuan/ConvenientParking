package com.example.convenientparking.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rental_package")
public class RentalPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numberOf;

    @ManyToOne
    @JoinColumn(name = "form_id")
    private RentalForm rentalForm;

    @OneToMany(mappedBy = "rentalPackage", cascade = CascadeType.ALL)
    private Set<ContractDetail> contractDetails;
}
