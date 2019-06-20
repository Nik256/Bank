package com.epam.lab.manager;

import com.epam.lab.counter.TransactionCounter;
import com.epam.lab.service.AccountService;
import com.epam.lab.transaction.Transaction;
import com.epam.lab.utils.TransferGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class TransactManager {
    private static final Logger logger = LogManager.getLogger(TransactManager.class.getName());
    private static final int ACCOUNTS_NUMBER = 10;
    private static final int THREAD_NUMBER = 20;
    private static final int TRANSACTION_NUMBER = 1000;
    private AccountService accountService = AccountService.getInstance();

    public void execute() {
        accountService.deleteAccounts();
        accountService.createAccounts(ACCOUNTS_NUMBER);
        accountService.showAccountsInfo();

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
        IntStream.range(0, TRANSACTION_NUMBER).forEach(i -> executorService.submit(
                new Transaction(TransferGenerator.generate())));

        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e);
            Thread.currentThread().interrupt();
        }
        accountService.showAccountsInfo();
        logger.info("Overall number of transactions: " + TransactionCounter.overall.get());
        logger.info("Skipped transactions: " + TransactionCounter.skipped.get());
        logger.info("Succeed transactions: " + TransactionCounter.succeed.get());
    }
}
