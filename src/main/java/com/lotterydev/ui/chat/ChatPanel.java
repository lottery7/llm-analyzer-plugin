package com.lotterydev.ui.chat;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.panels.VerticalLayout;
import com.lotterydev.chat.ChatService;
import com.theokanning.openai.completion.chat.UserMessage;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

@Slf4j
public class ChatPanel extends SimpleToolWindowPanel {

    private final ChatService chatService;
    private final JPanel messagesPanel;
    private final JScrollPane scrollPane;
    private final JBTextArea inputField;

    public ChatPanel(ChatService chatService) {
        super(true, true);

        this.chatService = chatService;

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new VerticalLayout(5));
        messagesPanel.setBackground(UIManager.getColor("EditorPane.background"));

        scrollPane = new JBScrollPane(messagesPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        inputField = new JBTextArea();
        inputField.setToolTipText("Введите сообщение...");
        inputField.setLineWrap(true);
        inputField.setWrapStyleWord(true);
        inputField.setRows(3);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(JBColor.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (e.isShiftDown()) {
                        inputField.append("\n");
                        e.consume();
                    } else {
                        sendMessage();
                        e.consume();
                    }
                }
            }
        });


        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputField, BorderLayout.SOUTH);

        setContent(mainPanel);
    }

    public ChatPanel(ChatService chatService, String firstMessage) {
        this(chatService);
        inputField.setText(firstMessage);
        sendMessage();
    }

    private void sendMessage() {
        inputField.setEditable(false);
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            addMessage(new MessageComponent(MessageComponent.Sender.USER, text));
            inputField.setText("");
            reply(text);
        }
    }

    private void reply(String userInput) {
        SwingUtilities.invokeLater(() -> {
            MessageComponent message = new MessageComponent(
                    MessageComponent.Sender.ASSISTANT, "**Thinking...**");
            addMessage(message);

            StringBuffer accContent = new StringBuffer();

            chatService.sendMessageStream(new UserMessage(userInput)).subscribe(chunk -> {
                        if (chunk != null && chunk.getChoices() != null) {
                            String chunkContent = chunk.getChoices().get(0).getMessage().getTextContent();
                            if (chunkContent != null && !chunkContent.isEmpty()) {
                                accContent.append(chunkContent);
                                message.setText(accContent.toString());
                                message.repaint();
                                scrollToEnd();
                            }
                        }
                    },

                    error -> {
                        addMessage(new MessageComponent(
                                MessageComponent.Sender.SYSTEM,
                                "**An unexpected error occurred:** " + error.getMessage()));
                        inputField.setEditable(true);
                    },

                    () -> inputField.setEditable(true));
        });
    }

    public void addMessage(MessageComponent message) {
        messagesPanel.add(message);
        messagesPanel.revalidate();
        messagesPanel.repaint();
        scrollToEnd();
    }

    private void scrollToEnd() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
}
