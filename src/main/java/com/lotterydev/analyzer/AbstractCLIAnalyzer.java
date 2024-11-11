// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.lotterydev.analyzer;

import com.lotterydev.exception.AnalysisException;
import com.lotterydev.model.AnalysisResult;

import java.nio.file.Path;

public abstract class AbstractCLIAnalyzer implements StaticCodeAnalyzer {
    protected Path projectPath;
    protected Path outputFilePath;
    protected Path configPath;

    public AbstractCLIAnalyzer() {
    }

    public Path getConfigPath() {
        return configPath;
    }

    public void setConfigPath(Path configPath) {
        this.configPath = configPath.toAbsolutePath();
    }

    public Path getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(Path outputFilePath) {
        this.outputFilePath = outputFilePath.toAbsolutePath();
    }

    public Path getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(Path projectPath) {
        this.projectPath = projectPath.toAbsolutePath();
    }


    @Override
    public AnalysisResult analyze() throws AnalysisException {
        try {
            Process process = getProcessBuilder().start();
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                StringBuilder sb = new StringBuilder();
                try (var reader = process.errorReader()) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        sb.append('\n');
                    }
                }
                throw new RuntimeException(
                        "Analyzer finished with exit code " + exitCode + ":\n " + sb);
            }
            return AnalysisResult.fromPath(outputFilePath);
        } catch (Exception e) {
            throw new AnalysisException(e.getMessage());
        }
    }

    protected abstract ProcessBuilder getProcessBuilder() throws Exception;
}
