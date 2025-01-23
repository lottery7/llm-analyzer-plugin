package com.lotterydev.model;

import com.lotterydev.schema.AnalysisResults;
import lombok.Getter;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("LombokGetterMayBeUsed")
public class ResultsTableModel extends DefaultTableModel {
    private static final String[] columnNames = {"Weakness", "Position", "Description"};

    @Getter
    private final AnalysisResults analysisResults;

    public ResultsTableModel(AnalysisResults analysisResults) {
        this.analysisResults = analysisResults;

        String[][] tableData = analysisResults.getFindings().stream()
                .map(f -> new String[]{
                        f.getRule(),
                        "Lines " + f.getStartLineNumber() + "-" + f.getEndLineNumber(),
                        f.getDescription()
                })
                .toArray(String[][]::new);

        setDataVector(tableData, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
