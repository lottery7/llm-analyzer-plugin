// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.lotterydev.analyzer;

import com.lotterydev.exception.FileNotSpecifiedException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SemgrepAnalyzer extends AbstractCLIAnalyzer {

    public SemgrepAnalyzer() {
    }

    @Override
    protected ProcessBuilder getProcessBuilder()
            throws FileNotSpecifiedException, IOException {
        if (projectPath == null) {
            throw new FileNotSpecifiedException("Project path not specified");
        }

        if (outputFilePath == null) {
            throw new FileNotSpecifiedException("Output file not specified");
        }


        var args = new ArrayList<>(List.of("docker", "run", "--rm", "-v"));

        if (Files.isDirectory(projectPath)) {
            args.add(String.format("%s:/src", projectPath));
        } else {
            args.add(String.format("%s:/src/%s", projectPath, projectPath.getFileName()));
        }

        if (configPath != null) {
            args.add("-v");
            args.add(String.format("%s:/semgrep-config.yml", configPath));
        }

        if (Files.notExists(outputFilePath)) {
            Files.createFile(outputFilePath);
        }

        args.add("-v");
        args.add(String.format("%s:/semgrep-result.json", outputFilePath));

        args.add("semgrep/semgrep:latest");
        args.addAll(List.of(
                "semgrep", "scan",
                "--json",
                String.format("--config=%s", configPath != null ? "/semgrep-config.yml" : "auto"),
                "--output=/semgrep-result.json"
        ));

        return new ProcessBuilder(args);
    }
}
