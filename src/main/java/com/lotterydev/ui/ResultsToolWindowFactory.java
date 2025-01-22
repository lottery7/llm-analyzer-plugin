package com.lotterydev.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class ResultsToolWindowFactory implements ToolWindowFactory, DumbAware {
    public static final String TOOL_WINDOW_NAME = "Analysis Results";
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ResultsPanel resultsPanel = new ResultsPanel(project);

            Content content = ContentFactory.getInstance().createContent(resultsPanel, "", false);
            toolWindow.getContentManager().addContent(content);
        });
    }
}
