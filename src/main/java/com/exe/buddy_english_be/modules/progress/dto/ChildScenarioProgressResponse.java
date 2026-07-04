package com.exe.buddy_english_be.modules.progress.dto;

import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;

import java.time.LocalDateTime;

public record ChildScenarioProgressResponse(
        Long id,
        Long childId,
        Long scenarioId,
        String scenarioTitle,
        ProgressStatus status,
        Integer score,
        Integer bestScore,
        Integer attemptCount,
        LocalDateTime lastPlayedAt,
        LocalDateTime completedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
