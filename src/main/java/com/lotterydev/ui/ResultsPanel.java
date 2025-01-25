package com.lotterydev.ui;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.lotterydev.analyzer.Analyzer;
import com.lotterydev.model.ResultsTableModel;
import com.lotterydev.schema.AnalysisResults;
import com.lotterydev.service.AnalyzeCodeEventService;
import com.lotterydev.service.AnalyzerService;
import com.lotterydev.util.Misc;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResultsPanel extends JPanel {
    private final Project project;

    private final ComboBox<String> selector;

    private final ResultsTable table;

    private final Gson gson = new Gson();

    public ResultsPanel(@NotNull Project project) {
        super(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder());

        this.project = project;

        AnalyzerService analyzerService = AnalyzerService.getInstance();

        selector = new ComboBox<>(analyzerService.getAllAnalyzersNames().toArray(new String[0]));
        selector.addActionListener(e -> {
            String analyzerName = (String) selector.getSelectedItem();
            if (analyzerName != null) {
                showResultsOf(analyzerService.getAnalyzerByName(analyzerName));
            }
        });

        table = new ResultsTable(project, getCurrentResultsModel());
        JScrollPane scrollPane = new JBScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(selector, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @NotNull
    private ResultsTableModel getResultsModel(@NotNull Analyzer analyzer) {
        var analyzeCodeActionService = AnalyzeCodeEventService.getInstance();
        Path resultsPath = analyzeCodeActionService.getResultsDir(project).resolve(analyzer.getResultsFileName());

        AnalysisResults analysisResults;

        try {
            Misc.reloadFileFromDisk(resultsPath);
            analysisResults = gson.fromJson(Files.readString(resultsPath), AnalysisResults.class);

        } catch (FileNotFoundException e) {
            analysisResults = new AnalysisResults();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return new ResultsTableModel(analysisResults);
    }

    @NotNull
    private ResultsTableModel getCurrentResultsModel() {
        var analyzerService = AnalyzerService.getInstance();
        Analyzer currentAnalyzer = analyzerService.getAnalyzerByName((String) selector.getSelectedItem());
        return getResultsModel(currentAnalyzer);
    }

    public void showResultsOf(@NotNull Analyzer analyzer) {
        String analyzerName = (String) selector.getSelectedItem();
        if (!analyzer.toString().equals(analyzerName)) {
            selector.setItem(analyzerName);
            return;  // previous line causes this method call
        }
        table.setModel(getResultsModel(analyzer));
    }
}
