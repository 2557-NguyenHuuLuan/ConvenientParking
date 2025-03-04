package com.example.convenientparking.Controllers.Admin;

import com.example.convenientparking.Entities.Contract;
import com.example.convenientparking.Entities.ContractDetail;
import com.example.convenientparking.Services.ContractService;
import com.example.convenientparking.Services.StatisticsService.RevenueStatisticsService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/revenue")
public class RevenueController {
    @Autowired
    private RevenueStatisticsService revenueStatisticsService;
    @Autowired
    private ContractService contractService;

    @GetMapping("/list-contracts")
    public String listContracts(@NotNull Model model) {
        List<Contract> contracts = contractService.getAllContracts();
        model.addAttribute("contracts", contracts);
        return "employee/listContracts";
    }

    @GetMapping("/contract/{contractId}")
    public String viewContractDetails(@PathVariable Long contractId, @NotNull Model model) {
        Optional<Contract> contractOptional = contractService.getContractById(contractId);
        if (contractOptional.isPresent()) {
            Contract contract = contractOptional.get();
            List<ContractDetail> contractDetails = contractService.getAllContractDetailByContractId(contractId);
            model.addAttribute("contract", contract);
            model.addAttribute("contractDetails", contractDetails);
            model.addAttribute("user", contract.getUser());
            return "employee/contractDetail";
        } else {
            model.addAttribute("message", "Không tìm thấy hợp đồng.");
            return "error";
        }
    }
    @GetMapping("/statistics-by-date")
    public String getRevenueByDate(@RequestParam LocalDate startDate,
                                   @RequestParam LocalDate endDate,
                                   Model model) {
        Long totalRevenue = revenueStatisticsService.revenueByDate(startDate, endDate);
        List<Contract> contracts = revenueStatisticsService.getPaidContractsByDate(startDate, endDate);

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("contracts", contracts);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "employee/revenue/revenueByDate";
    }

    @GetMapping("/statistics-by-month")
    public String getRevenueByMonth(@RequestParam Integer month,
                                    @RequestParam Integer year,
                                    Model model) {
        Long totalRevenue = revenueStatisticsService.revenueByMonth(month, year);
        List<Contract> contracts = revenueStatisticsService.getPaidContractsByMonth(month, year);

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("contracts", contracts);
        model.addAttribute("month", month);
        model.addAttribute("year", year);

        return "employee/revenue/revenueByMonth";
    }

    @GetMapping("/statistics-by-year")
    public String getRevenueByYear(@RequestParam Integer year, Model model) {
        Long totalRevenue = revenueStatisticsService.revenueByYear(year);
        List<Contract> contracts = revenueStatisticsService.getPaidContractsByYear(year);

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("contracts", contracts);
        model.addAttribute("year", year);

        return "employee/revenue/revenueByYear";
    }


}
