package com.exe.buddy_english_be.modules.progress.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ChildVocabularyProgressRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "vocabularyId is required")
        Long vocabularyId,

        @Min(value = 1, message = "masteryLevel must be at least 1")
        @Max(value = 5, message = "masteryLevel must be at most 5")
        Integer masteryLevel,

        @Min(value = 0, message = "correctCount must be at least 0")
        Integer correctCount,

        @Min(value = 0, message = "wrongCount must be at least 0")
        Integer wrongCount,

        @Min(value = 0, message = "confidenceScore must be at least 0")
        Double confidenceScore,

        LocalDateTime firstLearnedAt,
        LocalDateTime nextReviewAt,
        LocalDateTime lastPracticed
) {
}
