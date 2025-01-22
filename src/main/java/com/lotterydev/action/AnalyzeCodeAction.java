package com.lotterydev.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import com.lotterydev.service.AnalyzeCodeEventService;
import com.lotterydev.task.AnalyzeFileTask;
import org.jetbrains.annotations.NotNull;

public class AnalyzeCodeAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        AnalyzeCodeEventService service = AnalyzeCodeEventService.getInstance();
        ProgressManager.getInstance().run(new AnalyzeFileTask(service, event));
    }
}
