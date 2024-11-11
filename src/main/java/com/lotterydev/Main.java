// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: https://pvs-studio.com

package com.lotterydev;

import com.lotterydev.analyzer.LLMAnalyzer;
import com.lotterydev.analyzer.PVSStudioAnalyzer;
import com.lotterydev.analyzer.SemgrepAnalyzer;
import com.lotterydev.model.AnalysisResult;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        Path projectPath =
                Path.of("");

        SemgrepAnalyzer semgrepAnalyzer = new SemgrepAnalyzer();
        semgrepAnalyzer.setProjectPath(projectPath);
        semgrepAnalyzer.setConfigPath(Path.of("semgrep-config.yml"));
        semgrepAnalyzer.setOutputFilePath(Path.of("semgrep-result.json"));

        CompletableFuture<AnalysisResult> semgrepResultFuture =
                semgrepAnalyzer.analyzeAsync().handle((r, e) -> {
                    System.out.print("Semgrep:\n\n");
                    if (e != null) {
                        System.out.println(e.getMessage());
                    } else {
                        System.out.println(r);
                    }
                    System.out.print("\n\n");
                    return r;
                });

        PVSStudioAnalyzer pvsStudioAnalyzer = new PVSStudioAnalyzer();
        pvsStudioAnalyzer.setProjectPath(projectPath);
        pvsStudioAnalyzer.setOutputFilePath(Path.of("pvs-studio-result.json"));

        CompletableFuture<AnalysisResult> pvsStudioResultFuture =
                pvsStudioAnalyzer.analyzeAsync().handle((r, e) -> {
                    System.out.print("PVS Studio:\n\n");
                    if (e != null) {
                        System.out.println(e.getMessage());
                    } else {
                        System.out.println(r);
                    }
                    System.out.println("\n\n");
                    return r;
                });

        LLMAnalyzer llmAnalyzer = new LLMAnalyzer();
        llmAnalyzer.setProjectPath(projectPath);
        llmAnalyzer.setOutputFilePath(Path.of("llm-result.json"));
        llmAnalyzer.setGroqApiKey(System.getenv("GROQ_API_KEY"));

        CompletableFuture<AnalysisResult> llmResult = llmAnalyzer.analyzeAsync().thenApply(r -> {
            System.out.println(r);
            return r;
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return null;
        });


        System.out.println("Analysing...\r");

        try {
            semgrepResultFuture.join();
            pvsStudioResultFuture.join();
            llmResult.join();
        } catch (Exception ignored) {
        }
    }
}
