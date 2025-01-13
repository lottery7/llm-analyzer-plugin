package com.lotterydev.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.lotterydev.schemas.AnalysisResults;
import com.lotterydev.schemas.Finding;
import com.lotterydev.schemas.LLMFinding;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parsers {
    public static List<Finding> parseLLM(Path filePath) {
        try {
            Misc.reloadFileFromDisk(filePath);
            String json = new String(Files.readAllBytes(filePath));
            Gson gson = new Gson();
            // @formatter:off
            Type analysisResultsType = new TypeToken<AnalysisResults<LLMFinding>>() {}.getType();
            // @formatter:on
            AnalysisResults<LLMFinding> analysisResult = gson.fromJson(json, analysisResultsType);
            List<LLMFinding> llmFindings =
                    analysisResult.getFindings() != null ? analysisResult.getFindings() : new ArrayList<>();
            return llmFindings.stream().map(LLMFinding::toFinding).toList();
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return List.of(Finding.builder().description("Error loading results: " + e.getMessage()).build());
        }
    }
}
