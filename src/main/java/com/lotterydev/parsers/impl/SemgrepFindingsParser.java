package com.lotterydev.parsers.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lotterydev.parsers.AbstractReloadingFindingsParser;
import com.lotterydev.schemas.Finding;
import com.lotterydev.schemas.SemgrepFinding;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SemgrepFindingsParser extends AbstractReloadingFindingsParser<SemgrepFinding> {
    @Override
    protected JsonArray getResultsJsonArray(JsonObject json) {
        return json.getAsJsonArray("results");
    }

    @Override
    protected Type getAnalyzerListFindingsType() {
        // @formatter:off
        return new TypeToken<List<SemgrepFinding>>() {}.getType();
        // @formatter:on
    }

    @Override
    protected List<Finding> convertToFindings(@Nullable List<SemgrepFinding> analyzerFindings) {
        return analyzerFindings == null ?
                new ArrayList<>() : analyzerFindings.stream().map(SemgrepFinding::toFinding).toList();
    }
}
