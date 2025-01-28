package com.lotterydev.ui.results;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.table.JBTable;
import com.lotterydev.exception.InvalidToolWindowException;
import com.lotterydev.exception.ToolWindowNotFoundException;
import com.lotterydev.model.ResultsTableModel;
import com.lotterydev.schema.AnalysisResults;
import com.lotterydev.schema.Finding;
import com.lotterydev.ui.chat.ChatPanelFactory;
import com.lotterydev.ui.chat.ChatToolWindowFactory;
import com.lotterydev.ui.highlighter.Highlighter;
import com.lotterydev.ui.highlighter.HighlighterActionsRendererFactory;
import com.lotterydev.ui.highlighter.impl.AnalysisResultsHighlighter;
import com.lotterydev.ui.highlighter.impl.ExplainClearActionsRendererFactory;
import com.lotterydev.ui.highlighter.impl.HighlightManager;
import com.lotterydev.util.Misc;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class ResultsTable extends JBTable {
    private final Project project;
    private final HighlightManager highlightManager;
    private final HighlighterActionsRendererFactory factory;

    public ResultsTable(Project project, TableModel tableModel) {
        super();

        this.project = project;

        factory = new ExplainClearActionsRendererFactory(
                () -> ApplicationManager.getApplication().invokeLater(() -> {
                    String toolWindowName = ChatToolWindowFactory.TOOL_WINDOW_NAME;
                    ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(toolWindowName);
                    if (toolWindow == null) {
                        throw new ToolWindowNotFoundException(toolWindowName);
                    }
                    toolWindow.show(() -> onChatToolWindowShow(toolWindow));
                }));

        Highlighter highlighter = new AnalysisResultsHighlighter(factory);
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

    private void onChatToolWindowShow(ToolWindow toolWindow) {
        Content content = toolWindow.getContentManager().getContent(0);
        if (content == null) {
            throw new InvalidToolWindowException();
        }

        int selectedRow = getSingleSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        if (getModel() instanceof ResultsTableModel resultsTableModel) {
            AnalysisResults results = resultsTableModel.getAnalysisResults();

            String path = results.getFilePath();
            Finding finding = results.getFindings().get(selectedRow);

            try {
                String format = """
                        Explain weakness %s on lines from %d to %d in the following code:
                        ```java
                        %s
                        ```
                        """;
                String code = Misc.getEnumeratedCodeFromFile(Path.of(path));

                String prompt = String.format(format,
                        finding.getRule(),
                        finding.getStartLineNumber(),
                        finding.getEndLineNumber(),
                        code);


                content.setComponent(new ChatPanelFactory().createChatPanelWithMessage(prompt));
            } catch (IOException e) {
                log.trace("Error on reading code: ", e);
            }
        }


    }

    private void tryHighlightLines() {
        int selectedRow = getSingleSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        if (getModel() instanceof ResultsTableModel resultsTableModel) {
            AnalysisResults results = resultsTableModel.getAnalysisResults();

            String path = results.getFilePath();
            int lineStart = results.getFindings().get(selectedRow).getStartLineNumber();
            int lineEnd = results.getFindings().get(selectedRow).getEndLineNumber();

            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);

            highlightManager.highlightLines(project, file, lineStart, lineEnd);
        }
    }

    private int getSingleSelectedRow() {
        int[] selectedRows = getSelectedRows();
        int selectedColumn = getSelectedColumn();

        if (selectedRows.length != 1 || selectedColumn != 1) {
            return -1;
        }

        return selectedRows[0];
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
