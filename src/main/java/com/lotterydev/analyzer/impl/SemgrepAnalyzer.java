package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.AbstractDockerCLIAnalyzer;
import com.lotterydev.parser.FindingsParser;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SemgrepAnalyzer extends AbstractDockerCLIAnalyzer {
    private final FindingsParser parser;

    @Override
    public String getName() {
        return "semgrep";
    }

    @Override
    public String getPresentationName() {
        return "Semgrep";
    }

    @Override
    public String getResultsFileName() {
        return "semgrep-results.json";
    }

    @Override
    public FindingsParser getParser() {
        return parser;
    }

    @Override
    protected List<String> getCLICommand(String projectRoot, String resultsRoot) {
        String outputFilePath = String.format("%s/%s", resultsRoot, getResultsFileName());

        return List.of("semgrep", "--config", "auto", "--json",
                projectRoot, String.format("--output=%s", outputFilePath));
    }

    @Override
    protected String getImageTag() {
        return "semgrep/semgrep:latest";
    }
}
