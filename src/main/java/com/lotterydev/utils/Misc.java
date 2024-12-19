package com.lotterydev.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Misc {
    public static String getCodeFromFile(Path filePath) throws IOException {
        Counter lineCounter = new Counter();
        return Files.lines(filePath)
                .map(line -> String.format("%d. %s", lineCounter.incrementAndGet(), line))
                .collect(Collectors.joining("\n"));
    }
}
