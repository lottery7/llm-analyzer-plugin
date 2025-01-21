package com.lotterydev.parsers.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lotterydev.parsers.AbstractReloadingFindingsParser;
import com.lotterydev.schemas.CodeQLFinding;
import com.lotterydev.schemas.Finding;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CodeQLFindingsParser extends AbstractReloadingFindingsParser<CodeQLFinding> {
    @Override
    protected JsonArray getResultsJsonArray(JsonObject json) {
        return json.getAsJsonArray("runs").get(0)
                .getAsJsonObject()
                .getAsJsonArray("results");
    }

    @Override
    protected Type getAnalyzerListFindingsType() {
        // @formatter:off
        return new TypeToken<List<CodeQLFinding>>() {}.getType();
        // @formatter:on
    }

    @Override
    protected List<Finding> convertToFindings(@Nullable List<CodeQLFinding> analyzerFindings) {
        return analyzerFindings == null ? new ArrayList<>() :
                analyzerFindings.stream().flatMap(codeQLFinding -> codeQLFinding.toFindings().stream()).toList();
    }
}
