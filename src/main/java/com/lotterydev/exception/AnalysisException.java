package com.lotterydev.exception;

public class AnalysisException extends RuntimeException {
    public AnalysisException(Throwable cause) {
        super(cause.getMessage());
    }

    public AnalysisException() {
        super("Exception occurred during analysis");
    }
}
