package com.exe.buddy_english_be.modules.progress.dto;

import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ChildWorldProgressRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "worldId is required")
        Long worldId,

        ProgressStatus status,

        @Min(value = 0, message = "completionPercentage must be at least 0")
        @Max(value = 100, message = "completionPercentage must be at most 100")
        Integer completionPercentage,

        LocalDateTime lastPlayedAt,
        LocalDateTime unlockedAt
) {
}
