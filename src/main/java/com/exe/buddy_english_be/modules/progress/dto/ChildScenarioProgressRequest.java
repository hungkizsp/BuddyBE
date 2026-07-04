package com.exe.buddy_english_be.modules.progress.dto;

import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ChildScenarioProgressRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "scenarioId is required")
        Long scenarioId,

        ProgressStatus status,

        @Min(value = 0, message = "score must be at least 0")
        Integer score,

        @Min(value = 0, message = "bestScore must be at least 0")
        Integer bestScore,

        @Min(value = 0, message = "attemptCount must be at least 0")
        Integer attemptCount,

        LocalDateTime lastPlayedAt,
        LocalDateTime completedAt
) {
}
