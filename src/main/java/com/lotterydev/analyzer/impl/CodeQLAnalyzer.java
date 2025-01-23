package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.DockerCLIAnalyzer;
import com.lotterydev.parser.FindingsParser;
import com.lotterydev.parser.impl.CodeQLFindingsParser;
import com.lotterydev.schema.AnalysisResults;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@NoArgsConstructor
public class CodeQLAnalyzer extends DockerCLIAnalyzer {
    private final FindingsParser parser = CodeQLFindingsParser.getInstance();

    @Override
    public String toString() {
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

    @Override
    protected AnalysisResults parseResults(Path resultsFilePath) {
        return new AnalysisResults(toString(), null, parser.parse(resultsFilePath));
    }

    @Override
    public String getRawResultsFileName() {
        return "codeql-results.sarif";
    }

    @Override
    public String getResultsFileName() {
        return "codeql-results.json";
    }
}
