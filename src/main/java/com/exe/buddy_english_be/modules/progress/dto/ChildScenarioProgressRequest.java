package com.exe.buddy_english_be.modules.progress.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ChildScenarioProgressRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "scenarioId is required")
        Long scenarioId,

        @Size(max = 20, message = "status must be at most 20 characters")
        String status,

        @Min(value = 0, message = "attempts must be at least 0")
        Integer attempts,

        LocalDateTime completedAt
) {
}
