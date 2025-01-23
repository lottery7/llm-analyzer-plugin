package com.lotterydev.schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Finding {
    String rule;
    int startLineNumber;
    int endLineNumber;
    String description;
}
