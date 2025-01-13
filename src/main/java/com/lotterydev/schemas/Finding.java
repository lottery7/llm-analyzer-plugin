package com.lotterydev.schemas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finding {
    private final String rule;
    private final int startLineNumber;
    private final int endLineNumber;
    private final String description;
}
