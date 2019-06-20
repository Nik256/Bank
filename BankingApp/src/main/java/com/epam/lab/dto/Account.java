package com.epam.lab.dto;

import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account implements Serializable {
    private static final long serialversionUID = 129348938L;

    private Long id;
    private String username;
    private Long balance;
    private final Lock lock;

    public Account(Long id, String username, Long balance) {
        this.id = id;
        this.username = username;
        this.balance = balance;
        lock = new ReentrantLock();
    }

    public Long getId() {
        return id;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Lock getLock() {
        return lock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!id.equals(account.id)) return false;
        if (!username.equals(account.username)) return false;
        return balance.equals(account.balance);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + balance.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                '}';
    }
}
