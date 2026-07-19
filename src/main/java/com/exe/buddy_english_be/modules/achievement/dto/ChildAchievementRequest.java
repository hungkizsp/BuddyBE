package com.exe.buddy_english_be.modules.achievement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ChildAchievementRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "achievementId is required")
        Long achievementId,

        @Min(value = 0, message = "progressValue must be at least 0")
        Integer progressValue,

        LocalDateTime earnedAt
) {
}
