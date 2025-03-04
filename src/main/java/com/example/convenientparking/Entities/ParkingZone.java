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
@Table(name = "parking_zone")
public class ParkingZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 10, unique = true)
    @NotBlank(message = "Type name is required")
    @Size(min = 1, max = 10, message = "Name must be between 1 and 10 characters")
    private String name;
    @OneToMany(mappedBy = "parkingZone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParkingSpot> parkingSpots;
}
