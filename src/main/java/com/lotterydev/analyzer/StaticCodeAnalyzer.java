package com.lotterydev.analyzer;

import java.nio.file.Path;

public interface StaticCodeAnalyzer {
    String getName();
    void analyzeFile(Path filePath, Path resultsRootPath) throws Exception;
}
