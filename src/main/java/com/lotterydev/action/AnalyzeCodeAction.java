package com.lotterydev.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.lotterydev.service.AnalyzeCodeActionService;
import org.jetbrains.annotations.NotNull;

public class AnalyzeCodeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        AnalyzeCodeActionService service = AnalyzeCodeActionService.getInstance();

        ProgressManager.getInstance().run(new Task.Backgroundable(
                service.getProject(event), "Code analysis", false) {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                indicator.setText("Analyzing code...");
                service.analyzeSelectedFile(event);
                indicator.setText("Analysis completed!");
            }

            @Override
            public void onSuccess() {
                ApplicationManager.getApplication().invokeLater(() -> service.updateAndShowResultsToolWindow(event));
            }

            @Override
            public void onThrowable(@NotNull Throwable error) {
                Notification notification = new Notification(
                        "LLM Code Analysis Notification Group",
                        "Analysis error",
                        "An unexpected error occurred: " + error.getMessage(),
                        NotificationType.ERROR
                );
                Notifications.Bus.notify(notification);
            }
        });
    }
}
