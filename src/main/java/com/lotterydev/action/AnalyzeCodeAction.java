package com.lotterydev.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.NlsActions;
import com.lotterydev.service.AnalyzeCodeEventService;
import com.lotterydev.task.AnalyzeFileTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnalyzeCodeAction extends AnAction {
    public AnalyzeCodeAction(
            @Nullable @NlsActions.ActionText String text,
            @Nullable @NlsActions.ActionDescription String description) {
        super(text, description, null);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        AnalyzeCodeEventService service = AnalyzeCodeEventService.getInstance();
        ProgressManager.getInstance().run(new AnalyzeFileTask(service, event));
    }
}
