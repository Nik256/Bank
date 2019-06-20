package com.epam.lab.service;

import com.epam.lab.counter.TransactionCounter;
import com.epam.lab.dao.AccountDao;
import com.epam.lab.dto.Account;
import com.epam.lab.dto.Transfer;
import com.epam.lab.exception.InsufficientFundsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Lock;

public class TransactionService {
    private static final Logger logger = LogManager.getLogger(TransactionService.class.getName());
    private static AccountDao accountDao = AccountDao.getInstance();
    private static TransactionService instance;

    private TransactionService() {
    }

    public static TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    private void deposit(Account account, Long amount) {
        account.setBalance(account.getBalance() + amount);
        accountDao.save(account);
    }

    private void withdraw(Account account, Long amount) throws InsufficientFundsException {
        if ((account.getBalance() - amount) > 0) {
            account.setBalance(account.getBalance() - amount);
        } else {
            throw new InsufficientFundsException("Insufficient funds");
        }
        TransactionCounter.succeed.getAndIncrement();
        accountDao.save(account);
    }

    public void transfer(Transfer transfer) {
        Account fromAccount = transfer.getFrom();
        Account toAccount = transfer.getTo();
        Long amount = transfer.getAmount();
        // Preventing deadlock using lock ordering
        Lock lock1 = fromAccount.getId() < toAccount.getId() ? fromAccount.getLock() : toAccount.getLock();
        Lock lock2 = fromAccount.getId() < toAccount.getId() ? toAccount.getLock() : fromAccount.getLock();

        lock1.lock();
        lock2.lock();
        try {
            logger.info(Thread.currentThread().getName() + " / "+ fromAccount + " --" + amount + "--> " + toAccount);
            withdraw(fromAccount, amount);
            deposit(toAccount, amount);
        } catch (InsufficientFundsException e) {
            TransactionCounter.skipped.getAndIncrement();
            logger.info(e.getMessage());
        } finally {
            TransactionCounter.overall.getAndIncrement();
            lock2.unlock();
            lock1.unlock();
        }
    }
}
