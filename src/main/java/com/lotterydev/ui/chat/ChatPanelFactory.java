package com.lotterydev.ui.chat;

import com.lotterydev.chat.ChatHistoryManager;
import com.lotterydev.chat.ChatService;
import com.lotterydev.chat.impl.InMemoryChatHistoryManager;
import com.lotterydev.chat.impl.OpenAiChatService;
import com.lotterydev.util.Settings;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatPanelFactory {
    public ChatPanel createChatPanel() {
        ChatHistoryManager chatHistoryManager = new InMemoryChatHistoryManager();

        ChatService chatService = new OpenAiChatService(
                chatHistoryManager, Settings.getBaseUrl(), Settings.getApiKey(), Settings.getModel());

        return new ChatPanel(chatService);
    }

    public ChatPanel createChatPanelWithMessage(String message) {
        ChatHistoryManager chatHistoryManager = new InMemoryChatHistoryManager();

        ChatService chatService = new OpenAiChatService(
                chatHistoryManager, Settings.getBaseUrl(), Settings.getApiKey(), Settings.getModel());

        return new ChatPanel(chatService, message);
    }
}
