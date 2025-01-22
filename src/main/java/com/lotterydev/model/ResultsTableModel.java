package com.lotterydev.model;

import com.lotterydev.schema.Finding;

import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ResultsTableModel extends DefaultTableModel {
    private static final String[] columnNames = {"Weakness", "Position", "Description"};

    public ResultsTableModel(List<Finding> findings) {
        String[][] tableData = findings.stream()
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
