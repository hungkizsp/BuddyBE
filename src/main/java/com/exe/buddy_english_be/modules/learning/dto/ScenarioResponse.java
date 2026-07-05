package com.exe.buddy_english_be.modules.learning.dto;

import java.time.LocalDateTime;

public record ScenarioResponse(
        Long id,
        Long worldId,
        String worldName,
        String title,
        String description,
        String scenarioType,
        String expectedIntent,
        String difficulty,
        Integer orderIndex,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
