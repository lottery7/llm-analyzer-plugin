package com.lotterydev.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResults {
    String tool;
    String filePath;
    List<Finding> findings = new ArrayList<>();
}
