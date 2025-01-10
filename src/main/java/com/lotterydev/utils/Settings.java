package com.lotterydev.utils;


import com.google.gson.Gson;
import lombok.Data;
import lombok.Getter;

import java.io.InputStreamReader;
import java.io.Reader;

public class Settings {
    @Getter
    private static final String apiKey;

    @Getter
    private static final String baseUrl;

    @Getter
    private static final String model;

    @Getter
    private static final String dockerHost;

    @Getter
    private static final int timeoutSeconds;

    static {
        try (Reader reader = new InputStreamReader(Resources.getResourceInputStream("settings.json"))) {
            Gson gson = new Gson();
            Config config = gson.fromJson(reader, Config.class);

            apiKey = config.getApiKey();
            baseUrl = config.getBaseUrl();
            model = config.getModel();
            dockerHost = config.getDockerHost();
            timeoutSeconds = config.getTimeoutSeconds();
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load or parse configuration file (settings.json): " + e.getMessage(), e);
        }
    }


    @Data
    private static class Config {
        private String apiKey;
        private String baseUrl;
        private String model;
        private String dockerHost;
        private int timeoutSeconds;
    }
}
