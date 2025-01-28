package com.lotterydev.chat;

import com.theokanning.openai.completion.chat.ChatMessage;

import java.util.List;

public interface ChatHistoryManager {
    void addMessage(ChatMessage message);
    List<ChatMessage> getHistory();
    void clearHistory();
}
