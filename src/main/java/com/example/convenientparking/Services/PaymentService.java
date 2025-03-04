package com.example.convenientparking.Services;

import com.example.convenientparking.Entities.Contract;
import com.example.convenientparking.Entities.ContractDetail;
import com.example.convenientparking.Entities.Invoice;
import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Repositories.ContractDetailRepository;
import com.example.convenientparking.Repositories.ContractRepository;
import com.example.convenientparking.Repositories.InvoiceRepository;
import com.example.convenientparking.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private ContractDetailRepository contractDetailRepository;

    public void contractPayment(Long contractId, Long userId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new EntityNotFoundException("Contract not found"));
        List<ContractDetail> contractDetails = contractDetailRepository.findByContractId(contractId);
        deductMoney(contract.getValue(), userId);
        contract.setValidity("Đang có hiệu lực");
        contract.setStatus(true);
        for (ContractDetail contractDetail : contractDetails) {
            contractDetail.setStatus(true);
            contractDetailRepository.save(contractDetail);
        }
        createInvoice(contract);
        contract.setPaymentStatus(true);
        contractRepository.save(contract);
    }

    private Invoice createInvoice(Contract contract) {
        Invoice invoice = new Invoice();
        invoice.setContract(contract);
        invoice.setCreateOn(LocalDate.now());
        invoice.setTotalAmount(contract.getValue());
        invoiceRepository.save(invoice);
        return invoice;
    }

    private void deductMoney(Long amount, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getBalance() < amount) {
            throw new IllegalStateException("Tài khoản của bạn không đủ số dư để thanh toán.");
        }
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
    }
}
