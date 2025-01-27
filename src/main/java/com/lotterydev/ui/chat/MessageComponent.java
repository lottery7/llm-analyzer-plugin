package com.lotterydev.ui.chat;

import com.intellij.markdown.utils.MarkdownToHtmlConverterKt;
import com.intellij.util.ui.JBFont;

import javax.swing.*;
import java.awt.*;

public class MessageComponent extends JPanel {

    public MessageComponent(String sender, String markdownText) {
        setLayout(new BorderLayout());
        setBackground(sender.equals("User")
                ? UIManager.getColor("EditorPane.background").brighter()
                : UIManager.getColor("EditorPane.background"));

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15),
                BorderFactory.createLineBorder(getBackground(), 0)
        ));

        JLabel senderLabel = new JLabel(sender);
        senderLabel.setFont(senderLabel.getFont().deriveFont(Font.BOLD));
        add(senderLabel, BorderLayout.NORTH);

        JEditorPane messagePane = createMarkdownPane(markdownText);
        add(messagePane, BorderLayout.CENTER);
    }

    private JEditorPane createMarkdownPane(String markdownText) {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        editorPane.setOpaque(false);
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

        editorPane.setText(styledHtml);

        return editorPane;
    }
}
