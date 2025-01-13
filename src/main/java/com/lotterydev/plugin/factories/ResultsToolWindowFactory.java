package com.lotterydev.plugin.factories;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.util.Alarm;
import com.lotterydev.plugin.components.ResultsComponent;
import com.lotterydev.plugin.components.ResultsTable;
import com.lotterydev.plugin.components.models.ResultsTableModel;
import com.lotterydev.schemas.Finding;
import com.lotterydev.utils.Parsers;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ResultsToolWindowFactory implements ToolWindowFactory, DumbAware {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {


        Alarm alarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD);
        alarm.addRequest(() -> {
            Path resultsPath = Paths.get(project.getBasePath(), ".idea", "semgrep-results.json");
            List<Finding> findings = Parsers.parseSemgrep(resultsPath);


            ResultsTableModel tableModel = new ResultsTableModel(findings);
            ResultsTable table = new ResultsTable(tableModel);
            ResultsComponent resultsComponent = new ResultsComponent(table);

            Content content = ContentFactory.getInstance().createContent(resultsComponent, "", false);

            toolWindow.getContentManager().addContent(content);
        }, 0);
    }
}
