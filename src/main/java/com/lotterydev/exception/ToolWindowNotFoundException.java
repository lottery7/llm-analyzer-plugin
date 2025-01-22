package com.lotterydev.exception;

public class ToolWindowNotFoundException extends RuntimeException {
    public ToolWindowNotFoundException(String toolWindowName) {
        super("Couldn't find ToolWindow with name: " + toolWindowName);
    }
}
