package com.lotterydev.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

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
}
