package com.lotterydev.ui.highlighter.impl;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.markup.*;
import com.intellij.ui.JBColor;
import com.lotterydev.ui.highlighter.Highlighter;

import java.util.Arrays;

public class AnalysisResultsHighlighter implements Highlighter {
    private RangeHighlighter highlighter;

    @Override
    public void highlight(Editor editor, int startLine, int endLine) {
        MarkupModel markupModel = editor.getMarkupModel();
        int startOffset = editor.getDocument().getLineStartOffset(startLine - 1);
        int endOffset = editor.getDocument().getLineEndOffset(endLine - 1);

        TextAttributes textAttributes = new TextAttributes();
        textAttributes.setBackgroundColor(JBColor.YELLOW);

        highlighter = markupModel.addRangeHighlighter(
                startOffset,
                endOffset,
                HighlighterLayer.SELECTION - 1,
                textAttributes,
                HighlighterTargetArea.LINES_IN_RANGE
        );
    }

    @Override
    public void removeHighlight(Editor editor) {
        if (highlighter != null && highlighter.isValid()) {
            if (Arrays.asList(editor.getMarkupModel().getAllHighlighters()).contains(highlighter)) {
                editor.getMarkupModel().removeHighlighter(highlighter);
            }
        }
    }

    @Override
    public void scrollToHighlight(Editor editor) {
        if (editor != null && highlighter != null && highlighter.isValid()) {
            int startOffset = highlighter.getStartOffset();
            LogicalPosition logicalStartOffset = editor.offsetToLogicalPosition(startOffset);
            editor.getScrollingModel().scrollTo(logicalStartOffset, ScrollType.CENTER);
        }
    }
}
