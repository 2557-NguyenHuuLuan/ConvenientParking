package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.Contract;
import com.example.convenientparking.Entities.ContractDetail;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Services.ContractService;
import com.example.convenientparking.Services.PaymentService;
import com.example.convenientparking.Services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/my-contracts")
public class ContractController {

    @Autowired
    ContractService contractService;
    @Autowired
    UserService userService;
    @Autowired
    PaymentService paymentService;

    @GetMapping()
    public String getContracts(@NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        List<Contract> contracts = contractService.getContractsByUser(user.get());
        model.addAttribute("user", user);
        model.addAttribute("contracts", contracts);
        return "contract/myContracts";
    }
    @GetMapping("/{contractId}")
    public String viewContractDetails(@PathVariable Long contractId, @NotNull Model model) {
        Optional<Contract> contractOptional = contractService.getContractById(contractId);
        Optional<User> user = userService.getUserAuthentication();
        if (contractOptional.isPresent()) {
            Contract contract = contractOptional.get();
            List<ContractDetail> contractDetails = contractService.getAllContractDetailByContractId(contractId);
            model.addAttribute("contract", contract);
            model.addAttribute("contractDetails", contractDetails);
            model.addAttribute("user", user);
            return "contract/contractDetail";
        } else {
            model.addAttribute("message", "Không tìm thấy hợp đồng.");
            return "error";
        }
    }

    @GetMapping("/show-payment")
    public String showPaymentForm(@RequestParam Long contractId, @NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        Contract contract = contractService.findContractById(contractId);
        if (user.isPresent()) {
            model.addAttribute("contract", contract);
            model.addAttribute("user", user);
        }
        return "payment/paymentContract";
    }

    @PostMapping("/payment")
    public String makeContractPayment(@RequestParam("contractId") Long contractId,
                                      @RequestParam("password") String password,
                                      @NotNull Model model) {
        Optional<User> user = userService.getUserAuthentication();
        model.addAttribute("user", user);
        if (user.isPresent() && userService.verifyPassword(user.get(), password)) {
            try {
                paymentService.contractPayment(contractId, user.get().getId());
                model.addAttribute("message", "Thanh toán thành công. Hợp đồng đã được kích hoạt.");
            } catch (IllegalStateException | EntityNotFoundException e) {
                model.addAttribute("message", "Thanh toán thất bại: " + e.getMessage());
            }
        } else {
            model.addAttribute("message", "Thanh toán thất bại: Mật khẩu không đúng.");
        }
        return "payment/paymentResult";
    }


}
