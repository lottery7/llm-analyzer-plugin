package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.DockerCLIAnalyzer;
import com.lotterydev.parser.FindingsParser;
import com.lotterydev.parser.impl.SemgrepFindingsParser;
import com.lotterydev.schema.AnalysisResults;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.List;

@NoArgsConstructor
public class SemgrepAnalyzer extends DockerCLIAnalyzer {
    private final FindingsParser parser = SemgrepFindingsParser.getInstance();

    @Override
    public String toString() {
        return "Semgrep";
    }

    @Override
    protected AnalysisResults parseResults(Path resultsFilePath) {
        return new AnalysisResults(toString(), null, parser.parse(resultsFilePath));
    }

    @Override
    public String getRawResultsFileName() {
        return "raw-semgrep-results.json";
    }

    @Override
    public String getResultsFileName() {
        return "semgrep-results.json";
    }

    @Override
    protected List<String> getCLICommand(String projectRoot, String resultsRoot) {
        String outputFilePath = String.format("%s/%s", resultsRoot, getRawResultsFileName());

        return List.of("semgrep", "--config", "auto", "--json",
                projectRoot, String.format("--output=%s", outputFilePath));
    }

    @Override
    protected String getImageTag() {
        return "semgrep/semgrep:latest";
    }
}
