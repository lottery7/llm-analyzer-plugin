package com.lotterydev.analyzer.impl;

import com.google.gson.Gson;
import com.lotterydev.analyzer.Analyzer;
import com.lotterydev.parser.FindingsParser;
import com.lotterydev.parser.impl.LLMFindingsParser;
import com.lotterydev.schema.AnalysisResults;
import com.lotterydev.util.Misc;
import com.lotterydev.util.Resources;
import com.lotterydev.util.Settings;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class LLMAnalyzer extends Analyzer {
    private final Gson gson = new Gson();
    private final OpenAiService service = new OpenAiService(
            Settings.getApiKey(),
            Duration.of(Settings.getTimeoutSeconds(), ChronoUnit.SECONDS),
            Settings.getBaseUrl());
    private final FindingsParser parser = LLMFindingsParser.getInstance();

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

    protected ChatCompletionResult getChatCompletion(List<ChatMessage> messages) {
        var request = ChatCompletionRequest.builder()
                .model(Settings.getModel())
                .messages(messages)
                .n(1)
                .build();

        return service.createChatCompletion(request);
    }

    @Override
    public String toString() {
        return "Large Language Model";
    }

    @Override
    public String getRawResultsFileName() {
        return "raw-llm-results.json";
    }

    @Override
    public String getResultsFileName() {
        return "llm-results.json";
    }

    @Override
    public void analyzeFile(Path filePath, Path resultsRootPath) throws IOException {
        Path rawResultsFilePath = resultsRootPath.resolve(getRawResultsFileName());
        Path resultsFilePath = resultsRootPath.resolve(getResultsFileName());

        ChatMessage systemMessage = new SystemMessage(getSystemPrompt());
        ChatMessage userMessage = new UserMessage(getUserPrompt(Misc.getEnumeratedCodeFromFile(filePath)));

        var chatResponse = getChatCompletion(List.of(systemMessage, userMessage));

        String responseContent = chatResponse.getChoices().get(0).getMessage().getContent();
        String jsonPart = parseJsonPart(responseContent);

        Files.writeString(rawResultsFilePath, jsonPart);

        AnalysisResults analysisResults = new AnalysisResults(
                Settings.getModel(),
                filePath.toString(),
                parser.parse(rawResultsFilePath));

        Files.writeString(resultsFilePath, gson.toJson(analysisResults));
    }

}
