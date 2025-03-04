package com.example.convenientparking.Services;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class RandomCodeTransactionService {
    private final String defaultCharacters = "abcdefghijklmnopqrstuvwxyz123456789";
    private final int defaultLength = 12;
    private final SecureRandom random = new SecureRandom();

    public String generateRandomString() {
        return generateRandomString(defaultLength, defaultCharacters);
    }
    public String generateRandomString(int length, String characters) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
