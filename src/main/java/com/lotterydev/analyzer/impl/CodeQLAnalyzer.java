package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.AbstractDockerCLIAnalyzer;
import com.lotterydev.parsers.FindingsParser;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CodeQLAnalyzer extends AbstractDockerCLIAnalyzer {
    private final FindingsParser parser;

    @Override
    protected List<String> getCLICommand(String projectRoot, String resultsRoot) {
        return List.of("security", "--language", "java", "--override");
    }

    @Override
    protected String getImageTag() {
        return "codeql";
    }

    @Override
    public String getName() {
        return "codeql";
    }

    @Override
    public String getPresentationName() {
        return "CodeQL";
    }

    @Override
    public String getResultsFileName() {
        return "codeql-results.sarif";
    }

    @Override
    public FindingsParser getParser() {
        return parser;
    }
}
