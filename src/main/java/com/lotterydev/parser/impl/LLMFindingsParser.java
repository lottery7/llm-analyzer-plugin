package com.lotterydev.parser.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.lotterydev.parser.ReloadingFindingsParser;
import com.lotterydev.schema.Finding;
import com.lotterydev.schema.LLMFinding;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public final class LLMFindingsParser extends ReloadingFindingsParser<LLMFinding> {
    public static LLMFindingsParser getInstance() {
        return ApplicationManager.getApplication().getService(LLMFindingsParser.class);
    }

    @Override
    protected JsonArray getResultsJsonArray(JsonObject json) {
        return json.getAsJsonArray("findings");
    }

    @Override
    protected Type getAnalyzerListFindingsType() {
        // @formatter:off
        return new TypeToken<List<LLMFinding>>() {}.getType();
        // @formatter:on
    }

    @Override
    protected List<Finding> convertToFindings(@Nullable List<LLMFinding> analyzerFindings) {
        return analyzerFindings == null ?
                new ArrayList<>() : analyzerFindings.stream().map(LLMFinding::toFinding).toList();
    }
}
