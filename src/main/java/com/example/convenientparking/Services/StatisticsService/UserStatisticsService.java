package com.example.convenientparking.Services.StatisticsService;

import com.example.convenientparking.Entities.Contract;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Repositories.ContractRepository;
import com.example.convenientparking.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserStatisticsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;

    public List<User> getUsersIsCustomers() {
        return userRepository. findAllByUserRole();
    }

    public List<Contract> getAllContractsOfUser(Long userId) {
        return  contractRepository.findContractsByUser(userId);
    }
}
