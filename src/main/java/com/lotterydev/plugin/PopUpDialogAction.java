package com.lotterydev.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.lotterydev.Main;
import org.jetbrains.annotations.NotNull;

public class PopUpDialogAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Main.main(null);
    }
}
