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
@Table(name = "parking_spot")
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "location", length = 10, unique = true)
    @NotBlank(message = "Number plate is required")
    @Size(min = 1, max = 10, message = "Location must be between 1 and 10 characters")
    private String location;
    private boolean status;
    @ManyToOne
    @JoinColumn(name = "parking_zone_id")
    private ParkingZone parkingZone;
    @OneToMany(mappedBy = "parkingSpot", cascade = CascadeType.ALL)
    private Set<ContractDetail> contractDetails;
}
