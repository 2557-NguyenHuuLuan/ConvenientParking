package com.example.convenientparking.Services.StatisticsService;

import com.example.convenientparking.Entities.Contract;
import com.example.convenientparking.Repositories.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevenueStatisticsService {
    @Autowired
    private ContractRepository contractRepository;

    public List<Contract> getPaidContractsByDate(LocalDate startDate, LocalDate endDate) {
        return contractRepository.findContractsWithInvoiceBetweenDates(startDate, endDate);
    }
    public List<Contract> getPaidContractsByMonth(int month, int year) {
        return  contractRepository.findContractsByMonth(month, year);
    }

    public List<Contract> getPaidContractsByYear(int year) {
        return contractRepository.findContractsByYear(year);
    }

    public Long revenueByDate(LocalDate startDate, LocalDate endDate) {
        List<Contract> contracts = getPaidContractsByDate(startDate, endDate);
        Long revenue = 0L;
        for (Contract contract : contracts) {
            revenue +=  contract.getValue();
        }
        return  revenue;
    }
    public Long revenueByMonth(int month, int year) {
        List<Contract> contracts = getPaidContractsByMonth(month, year);
        Long revenue = 0L;
        for (Contract contract : contracts) {
            revenue +=  contract.getValue();
        }
        return  revenue;
    }
    public Long revenueByYear(int year) {
        List<Contract> contracts = getPaidContractsByYear(year);
        Long revenue = 0L;
        for (Contract contract : contracts) {
            revenue +=  contract.getValue();
        }
        return  revenue;
    }
}
