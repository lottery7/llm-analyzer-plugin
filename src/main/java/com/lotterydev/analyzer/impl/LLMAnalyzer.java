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
import java.util.List;

public class LLMAnalyzer implements StaticCodeAnalyzer {
    private static final OpenAiService service = new OpenAiService(
            Settings.getApiKey(),
            Settings.getBaseUrl());

    public LLMAnalyzer() {
    }

    public static ChatCompletionResult getChatCompletion(List<ChatMessage> messages) {
        var request = ChatCompletionRequest.builder()
                .model(Settings.getModel())
                .messages(messages)
                .n(1)
                .topP(0.5)
                .seed(0)
                .stream(false)
                .build();

        return service.createChatCompletion(request);
    }

    public static String getSystemPrompt() throws IOException {
        var is = Resources.getResourceInputStream("system-prompt.txt");
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static String getUserPrompt(String code) {
        return String.format("Here's the code to analyze:\n```\n%s\n```", code);
    }

    @Override
    public void analyzeFile(Path filePath, Path resultsRootPath) throws IOException {
        Path resultFilePath = resultsRootPath.resolve("llm-result.json");

        ChatMessage systemMessage = new SystemMessage(getSystemPrompt());
        ChatMessage userMessage = new UserMessage(getUserPrompt(Misc.getCodeFromFile(filePath)));

        var chatResponse = getChatCompletion(List.of(systemMessage, userMessage));

        String responseContent = chatResponse.getChoices().get(0).getMessage().getContent();

        Files.writeString(resultFilePath, responseContent);
    }

}
