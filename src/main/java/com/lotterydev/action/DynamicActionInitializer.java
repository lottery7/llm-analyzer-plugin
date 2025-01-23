package com.lotterydev.action;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.lotterydev.service.AnalyzerService;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicActionInitializer implements ProjectActivity {
    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        ActionManager actionManager = ActionManager.getInstance();

        DefaultActionGroup analyzeCodeGroup =
                (DefaultActionGroup) actionManager.getAction("AnalyzeCodeWithLLMGroup");
        analyzeCodeGroup.removeAll();

        AnalyzerService analyzerService = AnalyzerService.getInstance();
        analyzerService.getAllAnalyzersNames()
                .forEach(analyzerName -> {
                    String description = String.format("Static code analysis with %s", analyzerName);
                    AnAction action = new AnalyzeCodeAction(analyzerName, description);
                    analyzeCodeGroup.add(action);
                });

        return Unit.INSTANCE;
    }
}

