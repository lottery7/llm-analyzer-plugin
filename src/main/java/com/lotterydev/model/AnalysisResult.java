// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.lotterydev.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AnalysisResult {
    private String result;

    public AnalysisResult() {
    }

    public static AnalysisResult fromPath(Path path) throws IOException {
        return AnalysisResult.fromString(Files.readString(path));
    }

    public static AnalysisResult fromString(String result) {
        AnalysisResult analysisResult = new AnalysisResult();
        analysisResult.setResult(result);
        return analysisResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return result;
    }
}
