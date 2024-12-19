package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.AbstractDockerCLIAnalyzer;
import com.lotterydev.utils.Docker;

import java.io.IOException;
import java.util.List;

public class PVSStudioAnalyzer extends AbstractDockerCLIAnalyzer {
    private static final String imageName = "pvs-studio";

    static {
        if (!Docker.checkDockerImageExists(imageName)) {
            try {
                var ignored = createPVSStudioImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public PVSStudioAnalyzer() {
    }

    private static String createPVSStudioImage() throws IOException {
        return Docker.createImageFromDockerfile("PVS-Studio.Dockerfile", imageName);
    }

    @Override
    protected List<String> getCLICommand(String projectRoot, String resultsRoot) {
        String resultFilename = "pvs-studio-result.json";
        String resultFilePath = String.format("%s/%s", resultsRoot, resultFilename);

        // TODO move license to settings
        return List.of("java", "-jar", "/opt/pvs-studio-java/7.34.87460/pvs-studio.jar",
                "--license-key", "FREE-FREE-FREE-FREE", "--user-name", "PVS-Studio Free",
                "-s", projectRoot, "-o", resultFilePath, "-O", "json", "--disable-cache");
    }

    @Override
    protected String getImageTag() {
        return imageName;
    }
}
