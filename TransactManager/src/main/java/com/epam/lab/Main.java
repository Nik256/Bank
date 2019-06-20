package com.epam.lab;

import com.epam.lab.manager.TransactManager;

public class Main {
    public static void main(String[] args) {
        TransactManager transactManager = new TransactManager();
        transactManager.execute();
    }
}
