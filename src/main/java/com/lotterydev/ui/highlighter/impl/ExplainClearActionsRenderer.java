package com.lotterydev.ui.highlighter.impl;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.intellij.openapi.editor.Inlay;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseListener;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.lotterydev.ui.highlighter.InlineButton;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.MouseEvent;

@Slf4j
public class ExplainClearActionsRenderer implements EditorCustomElementRenderer {
    private static final int MARGIN = 5;

    private final InlineButton explainButton;
    private final InlineButton clearButton;
    private final Editor editor;
    private final FontMetrics fontMetrics;

    public ExplainClearActionsRenderer(Editor editor, Runnable onExplain, Runnable onClear) {
        this.editor = editor;
        this.fontMetrics = editor.getContentComponent().getFontMetrics(editor.getColorsScheme().getFont(EditorFontType.PLAIN));
        this.explainButton = new HighlighterInlineButton("Explain", fontMetrics, onExplain);
        this.clearButton = new HighlighterInlineButton("Clear", fontMetrics, onClear);
        addMouseListeners();
    }

    private static boolean checkAndSetButtonHover(InlineButton button, Point relativePoint) {
        if (button.contains(relativePoint)) {
            if (!button.isHovered()) {
                button.setHovered(true);
                return true;
            }
            return false;
        } else if (button.isHovered()) {
            button.setHovered(false);
            return true;
        }

        return false;
    }

    @Override
    public int calcWidthInPixels(@NotNull Inlay inlay) {
        return 4 * MARGIN + explainButton.getWidth() + clearButton.getWidth();
    }

    @Override
    public int calcHeightInPixels(@NotNull Inlay inlay) {
        return fontMetrics.getHeight() + 2 * MARGIN;
    }

    @Override
    public void paint(@NotNull Inlay inlay, Graphics g, Rectangle targetRegion, @NotNull TextAttributes textAttributes) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            EditorColorsScheme colorsScheme = EditorColorsManager.getInstance().getGlobalScheme();
            Font font = colorsScheme.getFont(EditorFontType.PLAIN);
            g2d.setFont(font);

            int x = targetRegion.x + MARGIN;
            int y = targetRegion.y + MARGIN + fontMetrics.getAscent();

            g2d.setColor(JBColor.BLUE);
            explainButton.draw(g2d, x, y);

            x += explainButton.getWidth() + 2 * MARGIN;

            g2d.setColor(JBColor.RED);
            clearButton.draw(g2d, x, y);

        } finally {
            g2d.dispose();
        }
    }

    private void addMouseListeners() {
        editor.addEditorMouseListener(new EditorMouseListener() {
            @Override
            public void mouseClicked(@NotNull EditorMouseEvent event) {
                handleClick(event.getMouseEvent());
            }
        });

        editor.addEditorMouseMotionListener(new EditorMouseMotionListener() {
            @Override
            public void mouseMoved(@NotNull EditorMouseEvent event) {
                handleHover(event.getMouseEvent());
            }
        });
    }

    private void handleClick(MouseEvent ignored) {
        if (explainButton.isHovered()) {
            explainButton.onClick();
        } else if (clearButton.isHovered()) {
            clearButton.onClick();
        }
    }

    private void handleHover(MouseEvent event) {
        boolean hasChanges = false;

        Point point = event.getPoint();
        Inlay<?> inlay = editor.getInlayModel().getElementAt(point);


        if (event.getSource() instanceof EditorComponentImpl
                && inlay != null && inlay.getRenderer() == this && inlay.getBounds() != null) {

            Point relativePoint = new Point(
                    point.x - inlay.getBounds().x - MARGIN,
                    point.y - inlay.getBounds().y - MARGIN);

            hasChanges = checkAndSetButtonHover(explainButton, relativePoint);

            relativePoint = new Point(relativePoint.x - 2 * MARGIN - explainButton.getWidth(), relativePoint.y);

            hasChanges = hasChanges || checkAndSetButtonHover(clearButton, relativePoint);

        } else if (explainButton.isHovered() || clearButton.isHovered()) {
            explainButton.setHovered(false);
            clearButton.setHovered(false);
            hasChanges = true;
        }

        if (hasChanges) {
            editor.getContentComponent().setCursor(
                    explainButton.isHovered() || clearButton.isHovered()
                            ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                            : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
            );
            editor.getContentComponent().repaint();
        }
    }
}