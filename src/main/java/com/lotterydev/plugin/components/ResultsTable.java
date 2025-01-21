package com.lotterydev.plugin.components;

import com.intellij.ui.table.JBTable;
import com.lotterydev.plugin.components.models.ResultsTableModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

public class ResultsTable extends JBTable {
    public ResultsTable(ResultsTableModel tableModel) {
        super();

        setFillsViewportHeight(true);
        setAutoResizeMode(JBTable.AUTO_RESIZE_LAST_COLUMN);
        setModel(tableModel);
    }

    private int calculateMaxWidth(int columnIndex) {
        int maxWidth = 0;
        for (int row = 0; row < getRowCount(); row++) {
            Object value = getValueAt(row, columnIndex);
            if (value != null) {
                FontMetrics metrics = getFontMetrics(getFont());
                int width = metrics.stringWidth(value.toString());
                maxWidth = Math.max(maxWidth, width);
            }
        }
        return maxWidth;
    }

    private void setColumnsWidths() {
        for (int i = 0; i < 2; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            column.setMinWidth(60);
            column.setMaxWidth(280);

            int width = calculateMaxWidth(i) + 20;
            column.setPreferredWidth(width);
        }
    }

    @Override
    public void setModel(@NotNull TableModel model) {
        if (model.getColumnCount() == 0) return;
        super.setModel(model);
        setColumnsWidths();
    }
}
