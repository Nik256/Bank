package com.epam.lab.service;

import com.epam.lab.dao.AccountDao;
import com.epam.lab.dto.Account;
import com.epam.lab.utils.AccountGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountService {
    private static final Logger logger = LogManager.getLogger(AccountService.class.getName());
    private static AccountService instance;
    private static AccountDao accountDao = AccountDao.getInstance();

    private AccountService() {
    }

    public static AccountService getInstance() {
        if (instance == null) {
            instance = new AccountService();
        }
        return instance;
    }

    public void createAccounts(int accountNumber) {
        for (int i = 0; i < accountNumber; i++) {
            accountDao.save(AccountGenerator.generateAccount());
        }
        updateAccount();
    }

    private void updateAccount() {
        accountDao.updateAccountList();
    }

    public void deleteAccounts() {
        accountDao.deleteAll();
    }

    public void showAccountsInfo() {
        accountDao.getAll().forEach(logger::info);
        long sum = getTotalBalance();
        logger.info("Total Balance: " + sum);
    }

    public long getTotalBalance() {
        return accountDao.getAll().stream().mapToLong(Account::getBalance).sum();
    }
}
