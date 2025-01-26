package com.lotterydev.ui.highlighter;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;

public interface HighlighterActionsRendererFactory {
    EditorCustomElementRenderer createRenderer(Editor editor, Runnable onClear);
}
