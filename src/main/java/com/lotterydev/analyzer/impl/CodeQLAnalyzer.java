package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.AbstractDockerCLIAnalyzer;

import java.util.List;

public class CodeQLAnalyzer extends AbstractDockerCLIAnalyzer {
    public CodeQLAnalyzer() {
    }

    @Override
    public String getName() {
        return "CodeQL";
    }

    @Override
    protected List<String> getCLICommand(String projectRoot, String resultsRoot) {
        return List.of("security", "--language", "java", "--override");
    }

    @Override
    protected String getImageTag() {
        return "codeql";
    }
}
