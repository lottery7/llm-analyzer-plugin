package com.lotterydev.analyzer;

import com.lotterydev.parsers.FindingsParser;

import java.nio.file.Path;

public interface StaticCodeAnalyzer {
    String getName();

    String getPresentationName();

    String getResultsFileName();

    FindingsParser getParser();

    void analyzeFile(Path filePath, Path resultsRootPath) throws Exception;
}
