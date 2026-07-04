package com.exe.buddy_english_be.modules.learning.dto;

import java.time.LocalDateTime;

public record ScenarioVocabularyResponse(
        Long id,
        Long scenarioId,
        String scenarioTitle,
        Long vocabularyId,
        String word,
        String meaning,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
