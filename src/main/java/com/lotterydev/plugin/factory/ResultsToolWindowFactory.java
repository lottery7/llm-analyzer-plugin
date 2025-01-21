package com.lotterydev.plugin.factory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.lotterydev.plugin.component.ResultsComponent;
import org.jetbrains.annotations.NotNull;

public class ResultsToolWindowFactory implements ToolWindowFactory, DumbAware {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ApplicationManager.getApplication().invokeLater(() -> {
            ResultsComponent resultsComponent = new ResultsComponent(project);

            Content content = ContentFactory.getInstance().createContent(resultsComponent, "", false);
            toolWindow.getContentManager().addContent(content);
        });
    }
}
