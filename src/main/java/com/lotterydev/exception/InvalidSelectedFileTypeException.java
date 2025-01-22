package com.lotterydev.exception;

public class InvalidSelectedFileTypeException extends RuntimeException {
    public InvalidSelectedFileTypeException() {
        super("Selected file must have .java extension");
    }
}
