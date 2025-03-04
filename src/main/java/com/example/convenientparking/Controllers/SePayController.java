package com.example.convenientparking.Controllers;

import com.example.convenientparking.Entities.User;
import com.example.convenientparking.Services.RandomCodeTransactionService;
import com.example.convenientparking.Services.SePayService;
import com.example.convenientparking.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sepay")
public class SePayController {

    @Autowired
    private SePayService sePayService;
    @Autowired
    private RandomCodeTransactionService randomCodeTransactionService;
    @Autowired
    private UserService userService;

    @GetMapping("/transaction/{id}")
    public ResponseEntity<String> getTransactionDetails(@PathVariable String id) {
        String transactionDetails = sePayService.getTransactionDetails(id);
        return ResponseEntity.ok(transactionDetails);
    }

    @GetMapping("/transactions")
    public ResponseEntity<String> getTransactionList() {
        String transactionList = sePayService.getTransactionList();
        return ResponseEntity.ok(transactionList);
    }

    @GetMapping("/generate-qr")
    public ResponseEntity<Map<String, String>> generateQRCode(@RequestParam double amount) {
        String codeTransaction = randomCodeTransactionService.generateRandomString();
        String qrCodeUrl = sePayService.generateQRCode(amount, codeTransaction);
        Map<String, String> response = new HashMap<>();
        response.put("codeTransaction", codeTransaction);
        response.put("qrCodeUrl", qrCodeUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-payment/{codeTransaction}")
    public ResponseEntity<String> checkPayment(@PathVariable String codeTransaction) {
        String transactionList = sePayService.getTransactionList();
        boolean paymentReceived = sePayService.isPaymentReceived(transactionList, codeTransaction);
        return ResponseEntity.ok(paymentReceived ? "YES" : "NO");
    }

    @GetMapping("/deposit-into-account/{amount}")
    public ResponseEntity<String> depositIntoAccount(@PathVariable double amount) {
        Optional<User> user = userService.getUserAuthentication();
        if (user.isPresent()) {
            User currentUser = user.get();
            boolean depositSuccessful = userService.depositIntoAccount(currentUser, amount);
            if (depositSuccessful) {
                return ResponseEntity.ok("Deposit successful");
            } else {
                return ResponseEntity.status(500).body("Deposit failed");
            }
        }
        return ResponseEntity.status(401).body("User not authenticated");
    }

}
