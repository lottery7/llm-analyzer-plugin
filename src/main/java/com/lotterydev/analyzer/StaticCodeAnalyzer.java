package com.lotterydev.analyzer;

import java.nio.file.Path;

public interface StaticCodeAnalyzer {
    void analyzeFile(Path filePath, Path resultsRootPath) throws Exception;
}
