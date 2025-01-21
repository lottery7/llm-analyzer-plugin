package com.lotterydev.plugin.components;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBScrollPane;
import com.lotterydev.analyzer.AnalyzersService;
import com.lotterydev.analyzer.StaticCodeAnalyzer;
import com.lotterydev.plugin.components.models.ResultsTableModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResultsComponent extends JPanel {
    private final Project project;

    private final ComboBox<String> selector;

    private final ResultsTable table;
    private final JScrollPane scrollPane;

    public ResultsComponent(@NotNull Project project) {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder());

        this.project = project;

        var service = ApplicationManager.getApplication().getService(AnalyzersService.class);

        selector = new ComboBox<>(service.getAllPresentationNames().toArray(new String[0]));
        selector.addActionListener(e -> {
            String analyzerName = (String) selector.getSelectedItem();
            if (analyzerName != null) {
                showResultsOf(analyzerName);
            }
        });

        table = new ResultsTable(getCurrentResultsModel());
        scrollPane = new JBScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        add(selector, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    @NotNull
    private ResultsTableModel getResultsModel(@NotNull StaticCodeAnalyzer analyzer) {
        Path resultsPath = Paths.get(project.getBasePath(), ".idea", analyzer.getResultsFileName());
        return new ResultsTableModel(analyzer.getParser().parse(resultsPath));
    }

    @NotNull
    private ResultsTableModel getCurrentResultsModel() {
        var service = ApplicationManager.getApplication().getService(AnalyzersService.class);
        StaticCodeAnalyzer currentAnalyzer = service.getAnalyzerByPresentationName((String) selector.getSelectedItem());
        return getResultsModel(currentAnalyzer);
    }

    public void showResultsOf(@NotNull String analyzerPresentationName) {
        String selectedAnalyzerName = (String) selector.getSelectedItem();
        if (!analyzerPresentationName.equals(selectedAnalyzerName)) {
            selector.setItem(analyzerPresentationName);
            return;  // because selector.setItem causes this method call
        }
        var service = ApplicationManager.getApplication().getService(AnalyzersService.class);
        StaticCodeAnalyzer analyzer = service.getAnalyzerByPresentationName(analyzerPresentationName);
        table.setModel(getResultsModel(analyzer));
    }
}
