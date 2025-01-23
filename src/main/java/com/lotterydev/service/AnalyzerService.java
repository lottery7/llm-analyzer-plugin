package com.lotterydev.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.lotterydev.analyzer.Analyzer;
import com.lotterydev.analyzer.impl.CodeQLAnalyzer;
import com.lotterydev.analyzer.impl.LLMAnalyzer;
import com.lotterydev.analyzer.impl.SemgrepAnalyzer;
import com.lotterydev.exception.AnalyzerNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public final class AnalyzerService {
    private final List<Analyzer> analyzers;

    public AnalyzerService() {
        analyzers = List.of(new LLMAnalyzer(), new SemgrepAnalyzer(), new CodeQLAnalyzer());
        assert checkAllNamesDifferent();
    }

    public static AnalyzerService getInstance() {
        return ApplicationManager.getApplication().getService(AnalyzerService.class);
    }

    private boolean checkAllNamesDifferent() {
        Set<String> namesSet = new HashSet<>();
        for (var analyzer : analyzers) {
            if (!namesSet.add(analyzer.toString())) {
                return false;
            }
        }
        return true;
    }

    public @NotNull Analyzer getAnalyzerByName(String name) {
        return analyzers.stream()
                .filter(analyzer -> analyzer.toString().equals(name))
                .findAny()
                .orElseThrow(() -> new AnalyzerNotFoundException(name));
    }

    public List<String> getAllAnalyzersNames() {
        return analyzers.stream().map(Analyzer::toString).toList();
    }
}
