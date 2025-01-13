package com.lotterydev.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lotterydev.schemas.Finding;
import com.lotterydev.schemas.LLMFinding;
import com.lotterydev.schemas.SemgrepFinding;

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

            String rawJson = new String(Files.readAllBytes(filePath));

            Gson gson = new Gson();
            JsonArray results = gson.fromJson(rawJson, JsonObject.class).getAsJsonArray("findings");
            // @formatter:off
            List<LLMFinding> llmFindings = gson.fromJson(results, new TypeToken<List<LLMFinding>>() {}.getType());
            // @formatter:on

            return llmFindings == null ? new ArrayList<>() : llmFindings.stream().map(LLMFinding::toFinding).toList();
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return List.of(Finding.builder().description("Error loading results: " + e.getMessage()).build());
        }
    }

    public static List<Finding> parseSemgrep(Path filePath) {
        try {
            Misc.reloadFileFromDisk(filePath);

            String rawJson = new String(Files.readAllBytes(filePath));

            Gson gson = new Gson();
            JsonArray results = gson.fromJson(rawJson, JsonObject.class).getAsJsonArray("results");
            // @formatter:off
            Type semgrepFindingsType = new TypeToken<List<SemgrepFinding>>() {}.getType();
            // @formatter:on
            List<SemgrepFinding> semgrepFindings = gson.fromJson(results, semgrepFindingsType);

            return semgrepFindings == null ? new ArrayList<>() :
                    semgrepFindings.stream().map(SemgrepFinding::toFinding).toList();

        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return List.of(Finding.builder().description("Error loading results: " + e.getMessage()).build());
        }
    }
}
