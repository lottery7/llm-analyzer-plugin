package com.lotterydev.schemas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LLMFinding implements BaseFinding {
    private final int cwe;
    private final int startLineNumber;
    private final int endLineNumber;
    private final String description;

    public Finding toFinding() {
        return new Finding("CWE-" + cwe, startLineNumber, endLineNumber, description);
    }
}
