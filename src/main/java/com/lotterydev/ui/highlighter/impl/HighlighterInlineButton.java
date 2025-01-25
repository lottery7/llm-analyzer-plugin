package com.lotterydev.ui.highlighter.impl;

import com.lotterydev.ui.highlighter.InlineButton;

import java.awt.*;

public class HighlighterInlineButton implements InlineButton {
    private final String text;
    private final FontMetrics fontMetrics;
    private final Runnable onClick;
    private boolean isHovered = false;

    public HighlighterInlineButton(String text, FontMetrics fontMetrics, Runnable onClick) {
        this.text = text;
        this.fontMetrics = fontMetrics;
        this.onClick = onClick;
    }

    @Override
    public void draw(Graphics2D g2d, int x, int y) {
        if (isHovered()) {
            g2d.drawLine(x, y + 2, x + getWidth(), y + 2);
        }
        g2d.drawString(text, x, y);
    }

    @Override
    public boolean isHovered() {
        return isHovered;
    }

    @Override
    public void setHovered(boolean isHovered) {
        this.isHovered = isHovered;
    }

    @Override
    public int getWidth() {
        return fontMetrics.stringWidth(text);
    }

    @Override
    public int getHeight() {
        return fontMetrics.getHeight();
    }

    @Override
    public boolean contains(Point point) {
        return point.x >= 0 && point.x <= getWidth() && point.y >= 0 && point.y <= getHeight();
    }

    @Override
    public void onClick() {
        this.onClick.run();
    }
}

