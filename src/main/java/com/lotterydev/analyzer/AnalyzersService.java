package com.lotterydev.analyzer;

import com.intellij.openapi.components.Service;
import com.lotterydev.analyzer.impl.CodeQLAnalyzer;
import com.lotterydev.analyzer.impl.LLMAnalyzer;
import com.lotterydev.analyzer.impl.SemgrepAnalyzer;
import com.lotterydev.parser.impl.CodeQLFindingsParser;
import com.lotterydev.parser.impl.LLMFindingsParser;
import com.lotterydev.parser.impl.SemgrepFindingsParser;

import java.util.List;

@Service
public final class AnalyzersService {
    private static final List<StaticCodeAnalyzer> analyzers;

    static {
        analyzers = List.of(
                new LLMAnalyzer(new LLMFindingsParser()),
                new SemgrepAnalyzer(new SemgrepFindingsParser()),
                new CodeQLAnalyzer(new CodeQLFindingsParser()));
    }

    public StaticCodeAnalyzer getAnalyzerByPresentationName(String presentationName) {
        return analyzers.stream()
                .filter(analyzer -> analyzer.getPresentationName().equals(presentationName))
                .findAny()
                .orElseThrow(() -> new RuntimeException(
                        String.format("Cannot find analyzer with presentation name: \"%s\"", presentationName)));
    }

    public List<String> getAllPresentationNames() {
        return analyzers.stream().map(StaticCodeAnalyzer::getPresentationName).toList();
    }
}
