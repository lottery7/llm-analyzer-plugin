package com.lotterydev.ui.chat;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.panels.VerticalLayout;
import com.lotterydev.service.chat.ChatService;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
        scrollPane.setAutoscrolls(false);

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

    private void sendMessage() {
        inputField.setEditable(false);
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            addMessage(new MessageComponent("User", text));
            inputField.setText("");
            reply(text);
        }
    }

    private void reply(String userInput) {
        SwingUtilities.invokeLater(() -> {
            try {
                ChatMessage response = chatService.sendMessage(new UserMessage(userInput));
                addMessage(new MessageComponent("Assistant", response.getTextContent()));
            } catch (Throwable e) {
                addMessage(new MessageComponent("System",
                        "An unexpected error occurred: " + e.getMessage()));
            } finally {
                inputField.setEditable(true);
            }
        });
    }

    public void addMessage(MessageComponent message) {
        messagesPanel.add(message);
        messagesPanel.revalidate();
        messagesPanel.repaint();
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }
}
