package com.lotterydev.ui.highlighter.impl;

import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.markup.*;
import com.intellij.ui.JBColor;
import com.lotterydev.ui.highlighter.Highlighter;
import com.lotterydev.ui.highlighter.HighlighterActionsRendererFactory;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public class AnalysisResultsHighlighter implements Highlighter {
    private final HighlighterActionsRendererFactory elementRendererFactory;

    private RangeHighlighter highlighter;
    private Inlay<?> actionsInlay;

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

        EditorCustomElementRenderer renderer = elementRendererFactory.createRenderer(
                editor, () -> removeHighlight(editor));
        actionsInlay = editor.getInlayModel().addBlockElement(highlighter.getStartOffset(),
                true, true, 0, renderer);
    }

    @Override
    public void removeHighlight(Editor editor) {
        if (highlighter != null && highlighter.isValid()) {
            if (Arrays.asList(editor.getMarkupModel().getAllHighlighters()).contains(highlighter)) {
                editor.getMarkupModel().removeHighlighter(highlighter);
            }
        }

        if (actionsInlay != null && actionsInlay.isValid()) {
            actionsInlay.dispose();
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
