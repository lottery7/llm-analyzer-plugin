// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.lotterydev.exception;

public class FileNotSpecifiedException extends Exception {
    public FileNotSpecifiedException(String message) {
        super(message);
    }
}
