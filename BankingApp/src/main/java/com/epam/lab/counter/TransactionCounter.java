package com.epam.lab.counter;

import java.util.concurrent.atomic.AtomicInteger;

public class TransactionCounter {
    public static AtomicInteger overall = new AtomicInteger();
    public static AtomicInteger succeed = new AtomicInteger();
    public static AtomicInteger skipped = new AtomicInteger();
}
