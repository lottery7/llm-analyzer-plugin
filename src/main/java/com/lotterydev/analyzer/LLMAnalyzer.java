// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.lotterydev.analyzer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lotterydev.exception.AnalysisException;
import com.lotterydev.model.AnalysisResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class LLMAnalyzer implements StaticCodeAnalyzer {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private Path projectPath;
    private Path outputFilePath;
    private Path configPath;
    private String groqApiKey;

    public Path getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(Path projectPath) {
        this.projectPath = projectPath;
    }

    public Path getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(Path outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public Path getConfigPath() {
        return configPath;
    }

    public void setConfigPath(Path configPath) {
        this.configPath = configPath;
    }

    public String getGroqApiKey() {
        return groqApiKey;
    }

    public void setGroqApiKey(String groqApiKey) {
        this.groqApiKey = groqApiKey;
    }

    private JsonObject getJsonRequest() {
        JsonObject json = new JsonObject();
        json.addProperty("model", "llama3-8b-8192");

        JsonArray messages = new JsonArray();

        JsonObject message = new JsonObject();
        message.addProperty("role", "user");
        message.addProperty("content", "Explain the importance of fast language models");
        messages.add(message);

        json.add("messages", messages);
        return json;
    }

    private String getJsonResponseContent(JsonObject jsonResponse) throws AnalysisException {
        JsonArray choices = jsonResponse.getAsJsonArray("choices");
        if (choices.isEmpty()) {
            throw new AnalysisException("Groq didn't return any messages");
        }

        JsonObject firstChoice = choices.get(0).getAsJsonObject();
        JsonObject message = firstChoice.get("message").getAsJsonObject();
        return message.get("content").getAsString();
    }

    private HttpResponse<String> getResponseFromGroq(JsonObject jsonRequest) throws AnalysisException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.groq.com/openai/v1/chat/completions"))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + groqApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest.toString()))
                .build();

        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new AnalysisException(e.getMessage());
        }
    }

    @Override
    public AnalysisResult analyze() throws AnalysisException {
        Gson gson = new Gson();
        JsonObject jsonRequest = getJsonRequest();
        var response = getResponseFromGroq(jsonRequest);

        if (response.statusCode() != 200) {
            throw new AnalysisException(String.format("Groq returned code %d != 200", response.statusCode()));
        }

        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        String responseContent = getJsonResponseContent(jsonResponse);
        return AnalysisResult.fromString(responseContent);
    }
}
