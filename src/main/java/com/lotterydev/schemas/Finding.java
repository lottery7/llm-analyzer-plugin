package com.lotterydev.schemas;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Finding {
    private final int cwe;

    @SerializedName("start_line_number")
    private final int startLineNumber;

    @SerializedName("end_line_number")
    private final int endLineNumber;

    private final String description;
}
