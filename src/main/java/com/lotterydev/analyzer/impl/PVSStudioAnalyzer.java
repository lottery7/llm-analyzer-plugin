package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.AbstractDockerCLIAnalyzer;
import com.lotterydev.utils.Docker;
import com.lotterydev.utils.Settings;

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

        return List.of("java", "-jar", "/opt/pvs-studio-java/7.34.87460/pvs-studio.jar",
                "--license-key", Settings.getPvsStudioLicense().getLicenseKey(),
                "--user-name", Settings.getPvsStudioLicense().getUserName(),
                "-s", projectRoot, "-o", resultFilePath, "-O", "json", "--disable-cache");
    }

    @Override
    protected String getImageTag() {
        return imageName;
    }
}
