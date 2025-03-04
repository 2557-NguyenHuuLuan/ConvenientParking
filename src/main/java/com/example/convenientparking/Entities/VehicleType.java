package com.example.convenientparking.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicle_type")
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "typename", length = 50, unique = true)
    @NotBlank(message = "Type name is required")
    @Size(min = 1, max = 50, message = "Type name must be between 1 and 50 characters")
    private String typeName;
    private float coefficient;
    @OneToMany(mappedBy = "vehicleType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles;
}
