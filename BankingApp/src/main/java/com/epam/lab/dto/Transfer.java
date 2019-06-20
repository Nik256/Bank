package com.epam.lab.dto;

public class Transfer {
    private final Account from;
    private final Account to;
    private final Long amount;

    public Transfer(Account from, Account to, Long amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public Account getFrom() {
        return from;
    }

    public Account getTo() {
        return to;
    }

    public Long getAmount() {
        return amount;
    }
}
