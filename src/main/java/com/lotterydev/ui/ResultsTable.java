package com.lotterydev.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.table.JBTable;
import com.lotterydev.model.ResultsTableModel;
import com.lotterydev.schema.AnalysisResults;
import com.lotterydev.ui.highlighter.Highlighter;
import com.lotterydev.ui.highlighter.impl.AnalysisResultsHighlighter;
import com.lotterydev.ui.highlighter.impl.HighlightManager;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class ResultsTable extends JBTable {
    private final Project project;
    private final HighlightManager highlightManager;

    public ResultsTable(Project project, TableModel tableModel) {
        super();

        this.project = project;

        Highlighter highlighter = new AnalysisResultsHighlighter();
        highlightManager = new HighlightManager(highlighter);

        setFillsViewportHeight(true);
        setAutoResizeMode(JBTable.AUTO_RESIZE_LAST_COLUMN);
        setModel(tableModel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tryHighlightLines();
            }
        });
    }

    private void tryHighlightLines() {
        int[] selectedRows = getSelectedRows();
        int selectedColumn = getSelectedColumn();

        if (selectedRows.length != 1 || selectedColumn != 1) {
            return;
        }

        int selectedRow = selectedRows[0];

        if (getModel() instanceof ResultsTableModel resultsTableModel) {
            AnalysisResults results = resultsTableModel.getAnalysisResults();

            String path = results.getFilePath();
            int lineStart = results.getFindings().get(selectedRow).getStartLineNumber();
            int lineEnd = results.getFindings().get(selectedRow).getEndLineNumber();

            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);

            highlightManager.highlightLines(project, file, lineStart, lineEnd);
        }
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
