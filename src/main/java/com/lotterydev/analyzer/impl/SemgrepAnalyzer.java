package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.AbstractDockerCLIAnalyzer;

import java.util.List;

public class SemgrepAnalyzer extends AbstractDockerCLIAnalyzer {
    public SemgrepAnalyzer() {
    }

    @Override
    public String getName() {
        return "Semgrep";
    }

    @Override
    protected List<String> getCLICommand(String projectRoot, String resultsRoot) {
        String outputFilename = "semgrep-result.json";
        String outputFilePath = String.format("%s/%s", resultsRoot, outputFilename);

        return List.of("semgrep", "--config", "auto", "--json",
                projectRoot, String.format("--output=%s", outputFilePath));
    }

    @Override
    protected String getImageTag() {
        return "semgrep/semgrep:latest";
    }
}
