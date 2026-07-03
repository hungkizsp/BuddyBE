package com.exe.buddy_english_be.modules.progress.dto;

import java.time.LocalDateTime;

public record ChildScenarioProgressResponse(
        Long id,
        Long childId,
        Long scenarioId,
        String scenarioTitle,
        String status,
        Integer attempts,
        LocalDateTime completedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
