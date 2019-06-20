package com.epam.lab.transaction;

import com.epam.lab.dto.Transfer;
import com.epam.lab.service.TransactionService;

public class Transaction implements Runnable {
    private TransactionService transactionService = TransactionService.getInstance();
    private Transfer transfer;

    public Transaction(Transfer transfer) {
        this.transfer = transfer;
    }

    @Override
    public void run() {
        transactionService.transfer(transfer);
    }
}
