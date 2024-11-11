// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.lotterydev.analyzer;

import com.lotterydev.exception.AnalysisException;
import com.lotterydev.model.AnalysisResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public interface StaticCodeAnalyzer {
    AnalysisResult analyze() throws AnalysisException;

    default CompletableFuture<AnalysisResult> analyzeAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return analyze();
            } catch (AnalysisException e) {
                throw new CompletionException(e);
            }
        });
    }
}
