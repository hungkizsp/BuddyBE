package com.exe.buddy_english_be.modules.analytics.dto;

import com.exe.buddy_english_be.modules.analytics.enums.SessionType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record LearningSessionRequest(
        @NotNull(message = "childId is required")
        Long childId,

        LocalDateTime startTime,

        LocalDateTime endTime,

        @NotNull(message = "sessionType is required")
        SessionType sessionType,

        Integer totalWordsSpoken,

        Integer totalAttempts
) {
}
