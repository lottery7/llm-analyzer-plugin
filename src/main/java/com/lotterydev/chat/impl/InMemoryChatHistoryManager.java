package com.lotterydev.chat.impl;

import com.lotterydev.chat.ChatHistoryManager;
import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryChatHistoryManager implements ChatHistoryManager {
    private final List<ChatMessage> history = new ArrayList<>();

    @Override
    public void addMessage(ChatMessage message) {
        history.add(message);
    }

    @Override
    public List<ChatMessage> getHistory() {
        return Collections.unmodifiableList(history);
    }

    @Override
    public void clearHistory() {
        history.clear();
    }
}
