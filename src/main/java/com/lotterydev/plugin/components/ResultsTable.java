package com.lotterydev.plugin.components;

import com.intellij.ui.table.JBTable;
import com.lotterydev.plugin.components.models.ResultsTableModel;

import javax.swing.table.TableColumn;
import java.awt.*;

public class ResultsTable extends JBTable {
    public ResultsTable(ResultsTableModel tableModel) {
        super(tableModel);

        for (int i = 0; i < 2; i++) {
            int width = calculateMaxWidth(i);
            setColumnWidth(i, width);
        }

        setFillsViewportHeight(true);
        setAutoResizeMode(JBTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private int calculateMaxWidth(int columnIndex) {
        int maxWidth = 60;
        for (int row = 0; row < getRowCount(); row++) {
            Object value = getValueAt(row, columnIndex);
            if (value != null) {
                FontMetrics metrics = getFontMetrics(getFont());
                int width = metrics.stringWidth(value.toString());
                maxWidth = Math.max(maxWidth, width);
            }
        }
        return maxWidth + 20;
    }

    private void setColumnWidth(int columnIndex, int width) {
        TableColumn column = getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
        column.setMaxWidth(width);
        column.setMinWidth(width);
    }
}
