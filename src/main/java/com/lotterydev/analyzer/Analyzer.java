package com.lotterydev.analyzer;

import java.nio.file.Path;

public abstract class Analyzer {
    @Override
    public abstract String toString();

    public abstract String getRawResultsFileName();

    public abstract String getResultsFileName();

    public abstract void analyzeFile(Path filePath, Path resultsRootPath) throws Exception;
}
