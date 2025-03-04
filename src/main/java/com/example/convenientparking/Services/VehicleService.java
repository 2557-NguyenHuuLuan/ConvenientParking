package com.example.convenientparking.Services;

import com.example.convenientparking.Entities.ContractDetail;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Entities.Vehicle;
import com.example.convenientparking.Repositories.ContractDetailRepository;
import com.example.convenientparking.Repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    private ContractDetailRepository contractDetailRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle>  getallVehicles() {
        return vehicleRepository.findAll();
    }

    public void saveVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }
    public List<Vehicle> findAllByUser(User user) {
        return vehicleRepository.findByUser(user);
    }
    public boolean existsByNumberPlate(String numberPlate) {
        return vehicleRepository.existsByNumberPlate(numberPlate);
    }
    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }
    public List<Vehicle> getVehiclesWithActiveContracts() {
        return vehicleRepository.findVehiclesWithActiveContracts();
    }

    public Optional<Vehicle> getVehicleById(Long id) {
        return vehicleRepository.findById(id);
    }
    public void deleteVehicle(Long vehicleId) {
        // Kiểm tra xem xe có tồn tại trong ContractDetail không
        if (contractDetailRepository.existsByVehicleId(vehicleId)) {
            throw new IllegalStateException("Không thể xóa xe vì xe đã được đăng ký gửi trong hợp đồng!");
        }

        // Nếu xe không tồn tại trong ContractDetail, thực hiện xóa xe
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalStateException("Xe không tồn tại"));

        vehicleRepository.delete(vehicle);
    }
}
