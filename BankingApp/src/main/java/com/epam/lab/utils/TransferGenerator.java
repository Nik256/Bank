package com.epam.lab.utils;

import com.epam.lab.dao.AccountDao;
import com.epam.lab.dto.Account;
import com.epam.lab.dto.Transfer;

import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TransferGenerator {
    private static List<File> files = FileUtils.getAllFiles();
    private static final Random random = new Random();
    private static AccountDao accountDao = AccountDao.getInstance();

    public static Transfer generate() {
        int fromRandomElementIndex = getRandomIndex();
        int toRandomElementIndex = getRandomIndex();
        while(fromRandomElementIndex == toRandomElementIndex) {
            toRandomElementIndex = getRandomIndex();
        }

        Account from = accountDao.getAccountList().get(fromRandomElementIndex);
        Account to = accountDao.getAccountList().get(toRandomElementIndex);
        Long amount = getRandomNumberInRange(1, 10);
        return new Transfer(from, to, amount);
    }

    private static int getRandomIndex() {
        return ThreadLocalRandom.current()
                .nextInt(files.size()) % files.size();
    }

    private static Long getRandomNumberInRange(long min, long max) {
        return random.longs(min, (max + 1))
                .findFirst()
                .getAsLong();
    }
}
