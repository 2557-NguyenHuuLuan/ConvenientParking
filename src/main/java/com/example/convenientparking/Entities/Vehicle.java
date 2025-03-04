package com.example.convenientparking.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "numberplate", length = 20, unique = true)
    @NotBlank(message = "Number plate is required")
    @Size(min = 1, max = 50, message = "Number plate must be between 1 and 50 characters")
    private String numberPlate;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "vehicle_type_id")
    private VehicleType vehicleType;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private Set<ContractDetail> contractDetails;

}
