package com.lotterydev.ui.highlighter.impl;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorCustomElementRenderer;
import com.lotterydev.ui.highlighter.HighlighterActionsRendererFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExplainClearActionsRendererFactory implements HighlighterActionsRendererFactory {
    private final Runnable onExplain;

    @Override
    public EditorCustomElementRenderer createRenderer(Editor editor, Runnable onClear) {
        return new ExplainClearActionsRenderer(editor, onExplain, onClear);
    }
}
