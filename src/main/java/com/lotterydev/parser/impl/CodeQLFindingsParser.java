package com.lotterydev.parser.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.lotterydev.parser.ReloadingFindingsParser;
import com.lotterydev.schema.CodeQLFinding;
import com.lotterydev.schema.Finding;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public final class CodeQLFindingsParser extends ReloadingFindingsParser<CodeQLFinding> {
    public static CodeQLFindingsParser getInstance() {
        return ApplicationManager.getApplication().getService(CodeQLFindingsParser.class);
    }

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
