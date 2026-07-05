package com.exe.buddy_english_be.modules.learning.dto;

import java.time.LocalDateTime;

public record ScenarioStepResponse(
        Long id,
        Long scenarioId,
        String scenarioTitle,
        Integer stepOrder,
        String buddyMessage,
        String expectedIntent,
        String expectedEntity,
        String successResponse,
        String failResponse,
        Long nextStepId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
