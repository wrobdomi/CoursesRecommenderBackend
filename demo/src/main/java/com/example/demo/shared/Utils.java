package com.example.demo.shared;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789abcdefghijklmnoprstwuxyzABCDEFGHIJKLMNOPRSTWUXYZ";

    public String generatePublicUserId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnedValue = new StringBuilder(length);

        for(int i = 0; i < length; i++){
            returnedValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnedValue);
    }


}
