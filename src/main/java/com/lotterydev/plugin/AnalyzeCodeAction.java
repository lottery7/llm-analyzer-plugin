package com.lotterydev.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.lotterydev.analyzer.StaticCodeAnalyzer;
import com.lotterydev.analyzer.impl.LLMAnalyzer;
import com.lotterydev.analyzer.impl.PVSStudioAnalyzer;
import com.lotterydev.analyzer.impl.SemgrepAnalyzer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Objects;

public class AnalyzeCodeAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        VirtualFile[] selectedFiles = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);

        if (selectedFiles == null || selectedFiles.length != 1) {
            Messages.showErrorDialog("Please, select exactly 1 file to perform LLM code analysis.",
                    "Invalid Files Selected");
            return;
        }

        VirtualFile selectedFile = selectedFiles[0];

        if (!selectedFile.getFileType().getDefaultExtension().equals("java")) {
            Messages.showErrorDialog("Selected file must have .java extension.",
                    "Invalid File Selected");
            return;
        }

        Path selectedFilePath = Path.of(selectedFile.getPath());

        Project project = Objects.requireNonNull(event.getProject());
        Path projectRootDirectory = Path.of(Objects.requireNonNull(project.getBasePath()));
        Path ideaFolderPath = Path.of(projectRootDirectory.toString(), ".idea");

        StaticCodeAnalyzer analyzer;

        switch (event.getPresentation().getText()) {
            case "LLM" -> analyzer = CodeAnalyzersHolder.llm;
            case "Semgrep" -> analyzer = CodeAnalyzersHolder.semgrep;
            case "PVS Studio" -> analyzer = CodeAnalyzersHolder.pvsStudio;
            default -> throw new RuntimeException("Choosed analyzer doesn't exist.");
        }

        ProgressManager.getInstance().run(new Task.Backgroundable(
                project,
                "Code analysis",
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
                Messages.showInfoMessage(
                        "Analyzing finished. Results saved in .idea folder.",
                        "Analysis Completed"
                );
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
        StaticCodeAnalyzer pvsStudio = new PVSStudioAnalyzer();
        StaticCodeAnalyzer llm = new LLMAnalyzer();
    }
}
