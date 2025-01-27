package com.lotterydev.service.chat.impl;

import com.lotterydev.service.chat.ChatHistoryManager;
import com.lotterydev.service.chat.ChatService;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class OpenAiChatService implements ChatService {
    private final ChatHistoryManager historyManager;
    private final String model;
    private final OpenAiService service;

    public OpenAiChatService(ChatHistoryManager historyManager, String baseUrl, String apiKey, String model) {
        this.historyManager = historyManager;
        this.model = model;
        this.service = new OpenAiService(apiKey, Duration.ofMinutes(2), baseUrl);
    }

    @Override
    public ChatMessage sendMessage(ChatMessage message) {
        historyManager.addMessage(message);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(historyManager.getHistory())
                .n(1)
                .build();

        ChatMessage response;

        response = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();

        historyManager.addMessage(response);

        return response;
    }

    @Override
    public Flowable<ChatCompletionChunk> sendMessageStream(ChatMessage message) {
        historyManager.addMessage(message);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(historyManager.getHistory())
                .n(1)
                .build();

        return service.streamChatCompletion(chatCompletionRequest);
    }
}
