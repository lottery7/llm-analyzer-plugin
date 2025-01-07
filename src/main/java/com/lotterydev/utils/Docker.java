package com.lotterydev.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Set;

public class Docker {
    private static final DockerClientConfig dockerConfig = DefaultDockerClientConfig
            .createDefaultConfigBuilder()
            .withDockerHost(Settings.getDockerHost())
            .build();


    private static final DockerClient dockerClientInstance = DockerClientBuilder
            .getInstance(dockerConfig)
            .build();


    public static DockerClient dockerClient() {
        return dockerClientInstance;
    }


    public static boolean checkDockerImageExists(String imageName) {
        return dockerClient()
                .listImagesCmd()
                .exec()
                .stream()
                .flatMap(image -> Arrays.stream(image.getRepoTags()))
                .anyMatch(tag -> tag.startsWith(imageName + ":"));
    }

    public static String createImageFromDockerfile(String dockerfileResourceFilename, String imageName) throws IOException {
        dockerfileResourceFilename = "docker/" + dockerfileResourceFilename;

        File dockerfile = Files.createTempDirectory("docker").resolve(".Dockerfile").toFile();

        try (InputStream is = Resources.getResourceInputStream(dockerfileResourceFilename);
             OutputStream os = new FileOutputStream(dockerfile)) {
            is.transferTo(os);
        }

        return Docker.dockerClient()
                .buildImageCmd()
                .withDockerfile(dockerfile)
                .withTags(Set.of(imageName))
                .exec(new BuildImageResultCallback())
                .awaitImageId();
    }
}
