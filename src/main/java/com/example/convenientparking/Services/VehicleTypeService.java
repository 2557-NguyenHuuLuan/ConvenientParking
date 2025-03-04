package com.example.convenientparking.Services;

import com.example.convenientparking.Entities.VehicleType;
import com.example.convenientparking.Repositories.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleTypeService {

    private final VehicleTypeRepository vehicleTypeRepository;

    @Autowired
    public VehicleTypeService(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    public VehicleType getVehicleTypeById(Long id) {
        return vehicleTypeRepository.findById(id).orElse(null);
    }

    public VehicleType addVehicleType(VehicleType vehicleType) {
        return vehicleTypeRepository.save(vehicleType);
    }

    public VehicleType updateVehicleType(Long id, VehicleType updatedVehicleType) {
        Optional<VehicleType> optionalVehicleType = vehicleTypeRepository.findById(id);
        if (optionalVehicleType.isPresent()) {
            VehicleType vehicleType = optionalVehicleType.get();
            vehicleType.setTypeName(updatedVehicleType.getTypeName());
            vehicleType.setCoefficient(updatedVehicleType.getCoefficient());
            return vehicleTypeRepository.save(vehicleType);
        }
        return null;
    }

    public void deleteVehicleType(Long id) {
        vehicleTypeRepository.deleteById(id);
    }

    public List<VehicleType> getAllVehicleTypes() {
        return vehicleTypeRepository.findAll();
    }
}
