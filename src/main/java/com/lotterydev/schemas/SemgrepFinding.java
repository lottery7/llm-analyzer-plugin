package com.lotterydev.schemas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SemgrepFinding {
    @SerializedName("check_id")
    String checkId;
    Region start;
    Region end;
    Extra extra;

    public Finding toFinding() {
        return new Finding(extra.metadata.cwe.get(0), start.line, Math.max(start.line, end.line), extra.message);
    }

    static class Region {
        int line;
    }

    static class Extra {
        String message;
        Metadata metadata;

        static class Metadata {
            List<String> cwe;
        }
    }
}


