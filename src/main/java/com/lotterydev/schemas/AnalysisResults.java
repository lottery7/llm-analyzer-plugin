package com.lotterydev.schemas;

import lombok.Data;

import java.util.List;

@Data
public class AnalysisResults<T extends BaseFinding> {
    private final String tool;
    private final List<T> findings;
}
