package com.epam.lab.dao;

import com.epam.lab.dto.Account;
import com.epam.lab.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AccountDao {
    private static final Logger logger = LogManager.getLogger(AccountDao.class.getName());
    private List<Account> accountList;
    private static AccountDao instance;

    private AccountDao() {
        accountList = getAll();
    }

    public static AccountDao getInstance() {
        setAccountsDirectoryPath();
        if (instance == null) {
            instance = new AccountDao();
        }
        return instance;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void updateAccountList() {
        this.accountList = getAll();
    }

    public void save(Account account) {
        FileUtils.writeAccountToFile(account, account.getId().toString());
    }

    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        for (File file : FileUtils.getAllFiles()) {
            accounts.add(FileUtils.readAccountFromFile(file.getName()));
        }
        return accounts;
    }

    public void deleteAll() {
        FileUtils.deleteAllFiles();
    }

    private static void setAccountsDirectoryPath() {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = loader.getResourceAsStream("config.properties")) {
            properties.load(inputStream);
            FileUtils.setFolderPath(properties.getProperty("accountsDirectoryPath"));
        } catch (IOException e) {
            logger.error(e + "Cant get properties");
        }
    }
}
