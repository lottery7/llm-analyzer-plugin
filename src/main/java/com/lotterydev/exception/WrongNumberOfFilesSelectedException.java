package com.lotterydev.exception;

public class WrongNumberOfFilesSelectedException extends RuntimeException {
    public WrongNumberOfFilesSelectedException() {
        super("You have to select exactly 1 file");
    }
}
