package com.epam.lab;

import com.epam.lab.counter.TransactionCounter;
import com.epam.lab.service.AccountService;
import com.epam.lab.transaction.Transaction;
import com.epam.lab.utils.TransferGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class TransactionTest {
    private AccountService accountService = AccountService.getInstance();

    @BeforeEach
    void setUp() {
        accountService.deleteAccounts();
        accountService.createAccounts(10);
    }

    @Test
    void initialTotalBalanceShouldBeEqualToFinalTotalBalance() throws InterruptedException {
        long initialTotalBalance = accountService.getTotalBalance();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        IntStream.range(0, 1000).forEach(i -> executorService.submit(
                new Transaction(TransferGenerator.generate())));
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        long finalTotalBalance = accountService.getTotalBalance();
        Assertions.assertEquals(initialTotalBalance, finalTotalBalance);
    }

    @Test
    void overallNumberOfTransactionsShouldBeEqualToSucceedPlusSkippedTransactions() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        IntStream.range(0, 1000).forEach(i -> executorService.submit(
                new Transaction(TransferGenerator.generate())));
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        Assertions.assertEquals(TransactionCounter.overall.get(), TransactionCounter.skipped.get() +
                TransactionCounter.succeed.getAndIncrement());
    }
}
