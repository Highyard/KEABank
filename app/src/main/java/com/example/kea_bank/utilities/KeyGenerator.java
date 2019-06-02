package com.example.kea_bank.utilities;

import java.util.ArrayList;

public class KeyGenerator {

    // Taken from https://dzone.com/articles/generate-random-alpha-numeric //
    private static final String ALPHA_NUMERIC_STRING = "0123456789";
    private static ArrayList<String> credArray = new ArrayList<>();
    private static final int keyLength = 3;
    public static final int keySize = 50;

    private static String randomAlphaNumeric(int count) {

        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        return builder.toString();
    }


    public static ArrayList<String> keyArray() {
        for (int i = 0; i < keySize; i++) {
            credArray.add(KeyGenerator.randomAlphaNumeric(keyLength)+ ":" + KeyGenerator.randomAlphaNumeric(keyLength));
        }
        return credArray;
    }

}
