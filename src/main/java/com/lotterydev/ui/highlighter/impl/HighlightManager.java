package com.lotterydev.ui.highlighter.impl;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.lotterydev.ui.highlighter.Highlighter;

public class HighlightManager {
    private final Highlighter highlighter;

    public HighlightManager(Highlighter highlighter) {
        this.highlighter = highlighter;
    }

    public void highlightLines(Project project, VirtualFile file, int startLine, int endLine) {
        Editor editor = FileEditorManager.getInstance(project).openTextEditor(new OpenFileDescriptor(project, file), true);
        if (editor != null) {
            highlighter.removeHighlight(editor);
            highlighter.highlight(editor, startLine, endLine);
            highlighter.scrollToHighlight(editor);
        }
    }

}
