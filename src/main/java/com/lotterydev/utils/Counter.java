package com.lotterydev.utils;

public class Counter {
    private long counter;

    public Counter(long initialValue) {
        counter = initialValue;
    }

    public Counter() {
        this(0);
    }

    public long incrementAndGet() {
        return ++counter;
    }
}
