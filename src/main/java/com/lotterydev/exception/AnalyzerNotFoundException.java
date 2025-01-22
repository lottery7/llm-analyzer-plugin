package com.lotterydev.exception;

public class AnalyzerNotFoundException extends RuntimeException {
    public AnalyzerNotFoundException(String name) {
        super("Couldn't find analyzer with name: " + name);
    }
}
