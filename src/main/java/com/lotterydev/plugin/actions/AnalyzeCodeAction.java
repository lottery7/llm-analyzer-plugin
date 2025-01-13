package com.lotterydev.plugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.util.Alarm;
import com.lotterydev.analyzer.StaticCodeAnalyzer;
import com.lotterydev.analyzer.impl.CodeQLAnalyzer;
import com.lotterydev.analyzer.impl.LLMAnalyzer;
import com.lotterydev.analyzer.impl.SemgrepAnalyzer;
import com.lotterydev.plugin.components.ResultsComponent;
import com.lotterydev.plugin.components.ResultsTable;
import com.lotterydev.plugin.components.models.ResultsTableModel;
import com.lotterydev.schemas.Finding;
import com.lotterydev.utils.Parsers;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class AnalyzeCodeAction extends AnAction {
    private static StaticCodeAnalyzer getSelectedAnalyzer(@NotNull AnActionEvent event) {
        String analyzerName = event.getPresentation().getText();

        return switch (analyzerName) {
            case "Large Language Model" -> CodeAnalyzersHolder.llm;
            case "Semgrep" -> CodeAnalyzersHolder.semgrep;
            case "CodeQL" -> CodeAnalyzersHolder.codeQL;
            default -> throw new RuntimeException("Chosen analyzer doesn't exist.");
        };
    }

    private static Path getSelectedFilePath(@NotNull AnActionEvent event) {
        VirtualFile[] selectedFiles = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (selectedFiles == null || selectedFiles.length != 1) {
            Messages.showErrorDialog("Please, select exactly 1 file to perform LLM code analysis.",
                    "Invalid Files Selected");
            return null;
        }

        VirtualFile selectedFile = selectedFiles[0];

        if (!selectedFile.getFileType().getDefaultExtension().equals("java")) {
            Messages.showErrorDialog("Selected file must have .java extension.",
                    "Invalid File Selected");
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

        Project project = Objects.requireNonNull(event.getProject());
        Path projectRootDirectory = Path.of(Objects.requireNonNull(project.getBasePath()));
        Path ideaFolderPath = Path.of(projectRootDirectory.toString(), ".idea");

        StaticCodeAnalyzer analyzer = getSelectedAnalyzer(event);

        ProgressManager.getInstance().run(new Task.Backgroundable(
                project,
                String.format("%s execution", analyzer.getName()),
                false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText("Analyzing code");

                try {
                    analyzer.analyzeFile(selectedFilePath, ideaFolderPath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                indicator.setText("Analysis completed Results saved in .idea folder.");
            }

            @Override
            public void onSuccess() {
                super.onSuccess();

                Alarm alarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD);
                alarm.addRequest(() -> {
                    ToolWindow toolWindow = ToolWindowManager.getInstance(event.getProject())
                            .getToolWindow("Analysis Results");

                    Content content = Objects.requireNonNull(toolWindow).getContentManager().getContent(0);


                    Path resultsFilePath = Paths.get(projectRootDirectory.toString(), ".idea", "llm-results.json");

                    List<Finding> findings = Parsers.parseLLM(resultsFilePath);
                    ResultsTableModel tableModel = new ResultsTableModel(findings);
                    ResultsTable table = new ResultsTable(tableModel);
                    ResultsComponent resultsComponent = new ResultsComponent(table);

                    Objects.requireNonNull(content).setComponent(resultsComponent);

                    toolWindow.show();
                }, 0);
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

    private interface CodeAnalyzersHolder {
        StaticCodeAnalyzer semgrep = new SemgrepAnalyzer();
        StaticCodeAnalyzer llm = new LLMAnalyzer();
        StaticCodeAnalyzer codeQL = new CodeQLAnalyzer();
    }
}
