package com.lotterydev.ui.highlighter;

import java.awt.*;

public interface InlineButton {
    void draw(Graphics2D g2d, int x, int y);

    boolean isHovered();

    void setHovered(boolean isHovered);

    int getWidth();

    int getHeight();

    boolean contains(Point point);

    void onClick();
}

