package com.lotterydev.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.lotterydev.analyzer.Analyzer;
import com.lotterydev.model.ResultsTableModel;
import com.lotterydev.service.AnalyzeCodeActionService;
import com.lotterydev.service.AnalyzerService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;

public class ResultsPanel extends JPanel {
    private final Project project;

    private final ComboBox<String> selector;

    private final ResultsTable table;

    public ResultsPanel(@NotNull Project project) {
        super(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder());

        this.project = project;

        AnalyzerService analyzerService = ApplicationManager.getApplication().getService(AnalyzerService.class);

        selector = new ComboBox<>(analyzerService.getAllAnalyzersNames().toArray(new String[0]));
        selector.addActionListener(e -> {
            String analyzerName = (String) selector.getSelectedItem();
            if (analyzerName != null) {
                showResultsOf(analyzerService.getAnalyzerByName(analyzerName));
            }
        });

        table = new ResultsTable(getCurrentResultsModel());
        JScrollPane scrollPane = new JBScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(selector, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @NotNull
    private ResultsTableModel getResultsModel(@NotNull Analyzer analyzer) {
        var analyzeCodeActionService = ApplicationManager.getApplication().getService(AnalyzeCodeActionService.class);
        Path resultsPath = analyzeCodeActionService.getResultsDir(project).resolve(analyzer.getResultsFileName());
        return new ResultsTableModel(analyzer.getParser().parse(resultsPath));
    }

    @NotNull
    private ResultsTableModel getCurrentResultsModel() {
        var service = ApplicationManager.getApplication().getService(AnalyzerService.class);
        Analyzer currentAnalyzer = service.getAnalyzerByName((String) selector.getSelectedItem());
        return getResultsModel(currentAnalyzer);
    }

    public void showResultsOf(@NotNull Analyzer analyzer) {
        table.setModel(getResultsModel(analyzer));
    }
}
