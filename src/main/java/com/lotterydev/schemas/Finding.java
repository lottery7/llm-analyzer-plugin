package com.lotterydev.schemas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Finding {
    private final String rule;
    private final int startLineNumber;
    private final int endLineNumber;
    private final String description;
}
