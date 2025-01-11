package com.lotterydev.schemas;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisResult {
    private final String tool;
    private final List<Finding> findings;
}
