package com.epam.lab.utils;

import com.epam.lab.dto.Account;

import java.util.Random;

public class AccountGenerator {
    private static final Random random = new Random();

    private static String getRandomString(int stringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        StringBuilder buffer = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int randomLimitedInt = leftLimit +
                    (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    private static long getRandomNumberInRange(long min, long max) {
        return random.longs(min, (max + 1))
                .findFirst()
                .getAsLong();
    }

    public static Account generateAccount() {
        return new Account(
                getRandomNumberInRange(0, 999999L),
                getRandomString(15),
                getRandomNumberInRange(0, 999L));
    }
}
