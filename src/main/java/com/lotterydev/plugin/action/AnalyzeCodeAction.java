package com.lotterydev.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.lotterydev.analyzer.AnalyzersService;
import com.lotterydev.analyzer.StaticCodeAnalyzer;
import com.lotterydev.plugin.component.ResultsComponent;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class AnalyzeCodeAction extends AnAction {
    private static Path getSelectedFilePath(@NotNull AnActionEvent event) {
        VirtualFile[] selectedFiles = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (selectedFiles == null || selectedFiles.length != 1) {
            Messages.showErrorDialog("Please, select exactly 1 file to perform LLM code analysis",
                    "Invalid Files Selected");
            return null;
        }

        VirtualFile selectedFile = selectedFiles[0];

        if (!selectedFile.getFileType().getDefaultExtension().equals("java")) {
            Messages.showErrorDialog("Selected file must have .java extension.", "Invalid File Selected");
            return null;
        }

        return Path.of(selectedFile.getPath());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Path selectedFilePath = getSelectedFilePath(event);
        if (selectedFilePath == null) {
            return;
        }

        Project project = event.getProject();
        if (project == null) {
            Messages.showErrorDialog("This plugin requires opened project.", "Project Is Not Opened");
            return;
        }

        Path ideaFolderPath = Path.of(project.getBasePath(), ".idea");

        StaticCodeAnalyzer analyzer = ApplicationManager.getApplication().getService(AnalyzersService.class)
                .getAnalyzerByPresentationName(event.getPresentation().getText());

        ProgressManager.getInstance().run(new Task.Backgroundable(
                project, String.format("%s execution", analyzer.getPresentationName()), false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText("Analyzing code...");

                try {
                    analyzer.analyzeFile(selectedFilePath, ideaFolderPath);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }

                indicator.setText("Analysis completed!");
            }

            @Override
            public void onSuccess() {
                super.onSuccess();

                ApplicationManager.getApplication().invokeLater(() -> {
                    ToolWindow toolWindow = ToolWindowManager.getInstance(event.getProject())
                            .getToolWindow("Analysis Results");

                    Content content = toolWindow.getContentManager().getContent(0);
                    ((ResultsComponent) content.getComponent()).showResultsOf(analyzer.getPresentationName());
                    toolWindow.show();
                });
            }

            @Override
            public void onThrowable(@NotNull Throwable error) {
                super.onThrowable(error);
                Messages.showErrorDialog(
                        String.format("Error happened during code analysis:\n\n%s", error.getMessage()),
                        "Error");
            }
        });


    }
}
