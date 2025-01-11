package com.lotterydev.plugin.components;

import javax.swing.*;

public class ResultsComponent extends JScrollPane {
    public ResultsComponent(ResultsTable resultsTable) {
        super(resultsTable);
        setBorder(BorderFactory.createEmptyBorder());
    }
}
