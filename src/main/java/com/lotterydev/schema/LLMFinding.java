package com.lotterydev.schema;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LLMFinding {
    int cwe;
    int startLineNumber;
    int endLineNumber;
    String description;

    public Finding toFinding() {

        return new Finding(
                "CWE-" + cwe, startLineNumber, Math.max(endLineNumber, startLineNumber), description);
    }
}
