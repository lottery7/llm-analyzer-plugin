package com.lotterydev.task;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.lotterydev.service.AnalyzeCodeEventService;
import org.jetbrains.annotations.NotNull;

public class AnalyzeFileTask extends Task.Backgroundable {
    private final AnalyzeCodeEventService analyzeCodeEventService;
    private final AnActionEvent event;

    public AnalyzeFileTask(@NotNull AnalyzeCodeEventService analyzeCodeEventService, @NotNull AnActionEvent event) {
        super(analyzeCodeEventService.getProject(event), "Code analysis", false);
        this.analyzeCodeEventService = analyzeCodeEventService;
        this.event = event;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        indicator.setIndeterminate(true);
        indicator.setText("Analyzing code...");
        analyzeCodeEventService.analyzeSelectedFile(event);
        indicator.setText("Analysis completed!");
    }

    @Override
    public void onSuccess() {
        ApplicationManager.getApplication().invokeLater(() ->
                analyzeCodeEventService.updateAndShowResultsToolWindow(event));
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
}
