package com.lotterydev.schema;

import lombok.Data;

import java.nio.file.Path;
import java.util.List;

@Data
public class AnalysisResults {
    private final String tool;
    private final Path filePath;
    private final List<Finding> findings;
}
