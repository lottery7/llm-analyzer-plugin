package com.lotterydev.analyzer;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.google.gson.Gson;
import com.lotterydev.schema.AnalysisResults;
import com.lotterydev.util.Docker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class DockerCLIAnalyzer extends Analyzer {
    private final Gson gson = new Gson();

    protected abstract List<String> getCLICommand(String projectRoot, String resultsRoot);

    protected abstract String getImageTag();

    protected abstract AnalysisResults parseResults(Path resultsFilePath);

    private int executeCommandInDocker(Path filePath, Path resultsRootPath) {
        String containerProjectRoot = String.format("/opt/src/%s", filePath.getFileName());
        String containerResultsRoot = "/opt/results";

        Volume projectVolume = new Volume(containerProjectRoot);
        Bind projectBind = new Bind(filePath.toString(), projectVolume);

        Volume resultsVolume = new Volume(containerResultsRoot);
        Bind resultsBind = new Bind(resultsRootPath.toString(), resultsVolume);

        HostConfig hostConfig = HostConfig.newHostConfig()
                .withBinds(projectBind, resultsBind)
                .withAutoRemove(true);

        CreateContainerResponse container;
        try {
            container = Docker.dockerClient()
                    .createContainerCmd(getImageTag())
                    .withHostConfig(hostConfig)
                    .withCmd(getCLICommand(containerProjectRoot, containerResultsRoot))
                    .exec();
        } catch (NotFoundException e) {
            throw new RuntimeException("Cannot find image with tag \"" + getImageTag() +
                    "\". Please, check README for instructions.");
        }

        Docker.dockerClient().startContainerCmd(container.getId()).exec();

        return Docker.dockerClient()
                .waitContainerCmd(container.getId())
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode();
    }

    private void writeAnalysisResults(Path filePath, Path resultsRootPath) throws IOException {
        Path rawResultsFilePath = resultsRootPath.resolve(getRawResultsFileName());
        Path resultsFilePath = resultsRootPath.resolve(getResultsFileName());

        AnalysisResults analysisResults = parseResults(rawResultsFilePath);
        analysisResults.setFilePath(filePath.toString());

        Files.writeString(resultsFilePath, gson.toJson(analysisResults));
    }

    @Override
    public void analyzeFile(Path filePath, Path resultsRootPath) {
        int exitCode = executeCommandInDocker(filePath, resultsRootPath);
        if (exitCode != 0) {
            throw new RuntimeException("Analyzer finished with exit code != 0: exit code = " + exitCode);
        }

        try {
            writeAnalysisResults(filePath, resultsRootPath);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
