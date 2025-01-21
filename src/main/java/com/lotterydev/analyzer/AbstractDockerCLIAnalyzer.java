package com.lotterydev.analyzer;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.lotterydev.util.Docker;

import java.nio.file.Path;
import java.util.List;

public abstract class AbstractDockerCLIAnalyzer implements StaticCodeAnalyzer {
    protected abstract List<String> getCLICommand(String projectRoot, String resultsRoot);

    protected abstract String getImageTag();

    @Override
    public void analyzeFile(Path filePath, Path resultsRootPath) {
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
                    .withWorkingDir("/project")  // TODO replace or remove
                    .withHostConfig(hostConfig)
                    .withCmd(getCLICommand(containerProjectRoot, containerResultsRoot))
                    .exec();
        } catch (NotFoundException e) {
            throw new RuntimeException("Cannot find image with tag \"" + getImageTag() +
                    "\". Please, check README for instructions.");
        }

        Docker.dockerClient().startContainerCmd(container.getId()).exec();

        int exitCode = Docker.dockerClient()
                .waitContainerCmd(container.getId())
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode();

        if (exitCode != 0) {
            throw new RuntimeException("Analyzer finished with exit code != 0: exit code = " + exitCode);
        }
    }
}
