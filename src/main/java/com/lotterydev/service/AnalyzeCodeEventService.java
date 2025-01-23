package com.lotterydev.service;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.project.ProjectKt;
import com.intellij.ui.content.Content;
import com.lotterydev.analyzer.Analyzer;
import com.lotterydev.exception.*;
import com.lotterydev.ui.ResultsPanel;
import com.lotterydev.ui.ResultsToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public final class AnalyzeCodeEventService {
    public static AnalyzeCodeEventService getInstance() {
        return ApplicationManager.getApplication().getService(AnalyzeCodeEventService.class);
    }

    public @NotNull Path getOneSelectedFilePath(@NotNull AnActionEvent event) {
        VirtualFile[] selectedFiles = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (selectedFiles == null || selectedFiles.length != 1) {
            throw new WrongNumberOfFilesSelectedException();
        }

        VirtualFile selectedFile = selectedFiles[0];

        if (!selectedFile.getFileType().getDefaultExtension().equals("java")) {
            throw new InvalidSelectedFileTypeException();
        }

        return Path.of(selectedFile.getPath());
    }

    public @NotNull Project getProject(@NotNull AnActionEvent event) {
        Project project = event.getProject();
        if (project == null) {
            throw new ProjectNotOpenedException();
        }
        return project;
    }

    public @NotNull Path getResultsDir(@NotNull Project project) {
        Path projectPath = ProjectKt.getStateStore(project).getProjectBasePath();
        Path resultsDirPath = projectPath.resolve(".llm-analyzer-plugin");

        try {
            Files.createDirectories(resultsDirPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return resultsDirPath;
    }

    public @NotNull Analyzer getSelectedAnalyzer(@NotNull AnActionEvent event) {
        return AnalyzerService.getInstance().getAnalyzerByName(event.getPresentation().getText());
    }

    public void analyzeSelectedFile(@NotNull AnActionEvent event) {
        Path selectedFilePath = getOneSelectedFilePath(event);

        Project project = getProject(event);
        Path resultsDir = getResultsDir(project);

        Analyzer analyzer = getSelectedAnalyzer(event);

        try {
            analyzer.analyzeFile(selectedFilePath, resultsDir);
        } catch (Exception e) {
            throw new AnalysisException(e);
        }
    }

    public void updateAndShowResultsToolWindow(@NotNull AnActionEvent event) {
        Analyzer analyzer = getSelectedAnalyzer(event);

        String toolWindowName = ResultsToolWindowFactory.TOOL_WINDOW_NAME;
        ToolWindow toolWindow = ToolWindowManager.getInstance(getProject(event)).getToolWindow(toolWindowName);
        if (toolWindow == null) {
            throw new ToolWindowNotFoundException(toolWindowName);
        }

        toolWindow.show(() -> {
            Content content = toolWindow.getContentManager().getContent(0);
            if (content == null) {
                throw new InvalidToolWindowException();
            }

            ResultsPanel resultsPanel = (ResultsPanel) content.getComponent();
            resultsPanel.showResultsOf(analyzer);
        });
    }
}
