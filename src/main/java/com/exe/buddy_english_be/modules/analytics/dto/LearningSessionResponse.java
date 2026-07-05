package com.exe.buddy_english_be.modules.analytics.dto;

import com.exe.buddy_english_be.modules.analytics.enums.SessionType;

import java.time.LocalDateTime;

public record LearningSessionResponse(
        Long id,
        Long childId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Long durationSeconds,
        SessionType sessionType,
        Integer totalWordsSpoken,
        Integer totalAttempts,
        LocalDateTime createdAt
) {
}
