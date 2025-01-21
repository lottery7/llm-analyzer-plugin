package com.lotterydev.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.lotterydev.schema.Finding;
import com.lotterydev.util.Misc;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractReloadingFindingsParser<T> implements FindingsParser {
    protected abstract JsonArray getResultsJsonArray(JsonObject json);

    protected abstract Type getAnalyzerListFindingsType();

    protected abstract List<Finding> convertToFindings(@Nullable List<T> analyzerFindings);

    @Override
    final public List<Finding> parse(Path filePath) {
        try {
            Misc.reloadFileFromDisk(filePath);
            String rawJson = new String(Files.readAllBytes(filePath));

            Gson gson = new Gson();
            JsonArray results = getResultsJsonArray(gson.fromJson(rawJson, JsonObject.class));
            List<T> analyzerFindings = gson.fromJson(results, getAnalyzerListFindingsType());

            return convertToFindings(analyzerFindings);
        } catch (Throwable e) {
            return FindingsParser.getErrorResults(e);
        }
    }
}
