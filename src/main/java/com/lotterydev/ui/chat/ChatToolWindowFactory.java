package com.lotterydev.ui.chat;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.lotterydev.service.chat.ChatHistoryManager;
import com.lotterydev.service.chat.ChatService;
import com.lotterydev.service.chat.impl.InMemoryChatHistoryManager;
import com.lotterydev.service.chat.impl.OpenAiChatService;
import com.lotterydev.util.Settings;
import org.jetbrains.annotations.NotNull;

public class ChatToolWindowFactory implements ToolWindowFactory, DumbAware {
    public static final String TOOL_WINDOW_NAME = "LLMChat";

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ChatHistoryManager chatHistoryManager = new InMemoryChatHistoryManager();

        ChatService chatService = new OpenAiChatService(
                chatHistoryManager, Settings.getBaseUrl(), Settings.getApiKey(), Settings.getModel());

        ChatPanel chatPanel = new ChatPanel(chatService);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(chatPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
