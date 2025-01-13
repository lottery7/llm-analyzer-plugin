package com.lotterydev.schemas;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisResults {
    private final String tool;
    private final List<Finding> findings;
}
