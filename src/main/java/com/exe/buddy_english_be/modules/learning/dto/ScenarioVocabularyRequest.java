package com.exe.buddy_english_be.modules.learning.dto;

import jakarta.validation.constraints.NotNull;

public record ScenarioVocabularyRequest(
        @NotNull(message = "scenarioId is required")
        Long scenarioId,

        @NotNull(message = "vocabularyId is required")
        Long vocabularyId
) {
}
