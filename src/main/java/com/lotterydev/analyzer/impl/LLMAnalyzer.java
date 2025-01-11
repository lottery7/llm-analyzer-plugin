package com.lotterydev.analyzer.impl;

import com.lotterydev.analyzer.StaticCodeAnalyzer;
import com.lotterydev.utils.Misc;
import com.lotterydev.utils.Resources;
import com.lotterydev.utils.Settings;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LLMAnalyzer implements StaticCodeAnalyzer {
    private static final OpenAiService service = new OpenAiService(
            Settings.getApiKey(),
            Duration.of(Settings.getTimeoutSeconds(), ChronoUnit.SECONDS),
            Settings.getBaseUrl());

    public LLMAnalyzer() {
    }

    public static ChatCompletionResult getChatCompletion(List<ChatMessage> messages) {
        var request = ChatCompletionRequest.builder()
                .model(Settings.getModel())
                .messages(messages)
                .n(1)
                .build();

        return service.createChatCompletion(request);
    }

    public static String getSystemPrompt() throws IOException {
        var is = Resources.getResourceInputStream("system-prompt.txt");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static String getUserPrompt(String code) {
        return String.format("Here's the code to analyze:\n```java\n%s\n```", code);
    }

    private static String parseJsonPart(String content) {
        String regex = "\\{.*\\}";
        Matcher matcher = Pattern.compile(regex, Pattern.DOTALL).matcher(content);

        if (!matcher.find()) {
            throw new RuntimeException("Couldn't find JSON in LLM response.");
        }

        return matcher.group();
    }

    @Override
    public String getName() {
        return "Large Language Model";
    }

    @Override
    public void analyzeFile(Path filePath, Path resultsRootPath) throws IOException {
        Path resultFilePath = resultsRootPath.resolve("llm-result.json");

        ChatMessage systemMessage = new SystemMessage(getSystemPrompt());
        ChatMessage userMessage = new UserMessage(getUserPrompt(Misc.getCodeFromFile(filePath)));

        var chatResponse = getChatCompletion(List.of(systemMessage, userMessage));

        String responseContent = chatResponse.getChoices().get(0).getMessage().getContent();
        String jsonPart = parseJsonPart(responseContent);

        Files.writeString(resultFilePath, jsonPart);
    }

}
