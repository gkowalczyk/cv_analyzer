package com.example.cv_analyzer.infrastructure.scraper.justjoinit;

import org.springframework.stereotype.Service;
import java.text.Normalizer;
import java.util.Map;

@Service
public class Validator {

    public static final Map<String, String> POLISH_REPLACEMENTS = Map.ofEntries(
            Map.entry("ą", "a"), Map.entry("ć", "c"), Map.entry("ę", "e"), Map.entry("ł", "l"),
            Map.entry("ń", "n"), Map.entry("ó", "o"), Map.entry("ś", "s"), Map.entry("ź", "z"),
            Map.entry("ż", "z")
    );

    public String validate(String input) {
        if (input == null || input.isBlank()) return "";

        String replaced = POLISH_REPLACEMENTS.entrySet().stream()
                .reduce(input, (acc, entry) ->
                                acc.replace(entry.getKey(), entry.getValue())
                                        .replace(entry.getKey().toUpperCase(), entry.getValue()),
                        (s1, s2) -> s2);
        String normalized = Normalizer.normalize(replaced, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }

    public String resolveCategory(String position) {
        return position == null || position.isBlank() ? "" : position.trim().split("\\s+")[0].toLowerCase();
    }
}
