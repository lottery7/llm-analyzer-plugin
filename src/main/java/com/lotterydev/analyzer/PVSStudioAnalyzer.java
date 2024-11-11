// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.lotterydev.analyzer;

import com.lotterydev.config.ApplicationConfig;
import com.lotterydev.exception.FileNotSpecifiedException;

import java.util.ArrayList;
import java.util.Arrays;

public class PVSStudioAnalyzer extends AbstractCLIAnalyzer {
    public PVSStudioAnalyzer() {
    }

    @Override
    protected ProcessBuilder getProcessBuilder() throws FileNotSpecifiedException {
        if (projectPath == null) {
            throw new FileNotSpecifiedException("Project path not specified");
        }

        if (outputFilePath == null) {
            throw new FileNotSpecifiedException("Output file not specified");
        }

        var args = new ArrayList<>(Arrays.asList(
                "java",
                "-jar", ApplicationConfig.PVS_STUDIO_JAR_PATH,
                "--src", projectPath.toString(),
                "--output-file", outputFilePath.toString()
        ));

        if (configPath != null) {
            args.add("--cfg");
            args.add(configPath.toString());
        }

        return new ProcessBuilder(args);
    }
}
