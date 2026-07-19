package com.exe.buddy_english_be.modules.analytics.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record WeeklyReportResponse(
        Long id,
        Long childId,
        LocalDate weekStart,
        LocalDate weekEnd,
        Integer learningMinutes,
        Integer wordsLearned,
        Integer speakingAttempts,
        Integer completedScenarios,
        LocalDateTime generatedAt,
        LocalDateTime createdAt
) {
}
