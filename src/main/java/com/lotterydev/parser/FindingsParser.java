package com.lotterydev.parser;

import com.lotterydev.schema.Finding;

import java.nio.file.Path;
import java.util.List;

public interface FindingsParser {
    static List<Finding> getErrorResults(Throwable e) {
        return List.of(Finding.builder().description("Error loading results: " + e.getMessage()).build());
    }

    List<Finding> parse(Path filePath);
}
