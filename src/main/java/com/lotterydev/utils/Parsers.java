package com.lotterydev.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lotterydev.schemas.AnalysisResult;
import com.lotterydev.schemas.Finding;

import java.io.IOException;
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
            AnalysisResult analysisResult = gson.fromJson(json, AnalysisResult.class);
            return analysisResult.getFindings() != null ? analysisResult.getFindings() : new ArrayList<>();
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
            return List.of(Finding.builder().description("Error loading results: " + e.getMessage()).build());
        }
    }
}
