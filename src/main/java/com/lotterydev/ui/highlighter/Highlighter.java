package com.lotterydev.ui.highlighter;

import com.intellij.openapi.editor.Editor;

public interface Highlighter {
    void highlight(Editor editor, int startLine, int endLine);

    void removeHighlight(Editor editor);

    void scrollToHighlight(Editor editor);
}
