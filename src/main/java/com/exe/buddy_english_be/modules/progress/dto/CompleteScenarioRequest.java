package com.exe.buddy_english_be.modules.progress.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CompleteScenarioRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "scenarioId is required")
        Long scenarioId,

        @Min(value = 0, message = "score must be at least 0")
        Integer score,

        // IDs of vocabulary words the child encountered during this scenario
        List<Long> vocabularyIds
) {
}
