package com.example.convenientparking.Services;
import com.example.convenientparking.Entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
@Service
public class SePayService {
    @Value("${sepay.api-token}")
    private String apiToken;
    @Value("${sepay.qr-url}")
    private String SEPAY_QR_URL;
    @Value("${sepay.bank-account}")
    private String BANK_ACCOUNT;
    @Value("${sepay.bank-name}")
    private String BANK_NAME;
    @Value("${sepay.detail-url}")
    private String SEPAY_DETAIL_URL;
    @Value("${sepay.list-url}")
    private String SEPAY_LIST_URL;

    public String getTransactionDetails(String transactionId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = SEPAY_DETAIL_URL + transactionId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        return response.getBody();
    }

    public String getTransactionList() {
        RestTemplate restTemplate = new RestTemplate();
        String url = SEPAY_LIST_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        return response.getBody();
    }

    public String generateQRCode(double amount, String codeTransaction) {
        String qrCodeUrl = String.format("%s?acc=%s&bank=%s&amount=%.2f&des=%s&template=compact&download=false",
                SEPAY_QR_URL, BANK_ACCOUNT, BANK_NAME, amount, codeTransaction);
        return qrCodeUrl;
    }

    public boolean isPaymentReceived(String transactionList, String codeTransaction) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(transactionList, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> transactions = (List<Map<String, Object>>) responseMap.get("transactions");
            for (Map<String, Object> transaction : transactions) {
                String transactionContent = (String) transaction.get("transaction_content");
                if (transactionContent != null) {
                    String code = transactionContent.substring(5, 17).trim();
                    if (code.equals(codeTransaction)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}