package com.lotterydev.ui.chat;

import com.intellij.markdown.utils.MarkdownToHtmlConverterKt;
import com.intellij.util.ui.JBFont;

import javax.swing.*;
import java.awt.*;

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

        JLabel senderLabel = new JLabel(sender.getValue());
        senderLabel.setFont(senderLabel.getFont().deriveFont(Font.BOLD));
        add(senderLabel, BorderLayout.NORTH);

        messagePane = createMarkdownPane();
        add(messagePane, BorderLayout.CENTER);
        setText(markdownText);
        revalidate();
        repaint();
    }

    private JEditorPane createMarkdownPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
        return editorPane;
    }

    public void setText(String markdownText) {
        Font font = JBFont.regular();
        assert font != null;

        String htmlText = MarkdownToHtmlConverterKt.convertMarkdownToHtml(markdownText);
        String styledHtml = """
                <html>
                    <head>
                        <style>
                            body {
                                font-family: '%s', sans-serif;
                                word-wrap: break-word;
                                word-break: break-word;
                                white-space: pre-wrap;
                                max-width: 100%%;
                            }
                        </style>
                    </head>
                    <body>
                        %s
                    </body>
                </html>
                """.formatted(font.getFamily(), htmlText);

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
