package com.lotterydev.ui.chat;

import com.intellij.markdown.utils.MarkdownToHtmlConverterKt;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBFont;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class MessageComponent extends JPanel {
    private final JEditorPane messagePane;

    public MessageComponent(Sender sender, String markdownText) {
        setLayout(new BorderLayout());
        setBackground(sender.equals(Sender.USER)
                ? UIManager.getColor("EditorPane.background").brighter()
                : UIManager.getColor("EditorPane.background"));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15),
                BorderFactory.createLineBorder(getBackground(), 0)
        ));

        JLabel senderLabel = new JBLabel(sender.getValue());
        senderLabel.setFont(senderLabel.getFont().deriveFont(Font.BOLD));
        add(senderLabel, BorderLayout.NORTH);

        messagePane = createMarkdownPane();
        add(messagePane, BorderLayout.CENTER);
        setText(markdownText);
    }

    private JEditorPane createMarkdownPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        return editorPane;
    }

    public void setText(String markdownText) {
        String htmlText = MarkdownToHtmlConverterKt.convertMarkdownToHtml(markdownText);
        String styledHtml = """
                <!DOCTYPE html>
                <html>
                    <head>
                        <style>
                            body {
                                font-family: '%s', sans-serif;
                                overflow-wrap: anywhere;
                                word-break: break-all;
                                white-space: pre-wrap;
                                max-width: 80%%;
                            }
                        </style>
                    </head>
                    <body>
                        %s
                    </body>
                </html>
                """.formatted(JBFont.regular().getFamily(), htmlText);

        messagePane.setText(styledHtml);
    }

    public enum Sender {
        USER("User"),
        ASSISTANT("AI Assistant"),
        SYSTEM("System");

        private final String value;

        Sender(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }
}
