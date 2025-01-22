package com.lotterydev.exception;

public class ProjectNotOpenedException extends RuntimeException {
    public ProjectNotOpenedException() {
        super("Project is not opened");
    }
}
