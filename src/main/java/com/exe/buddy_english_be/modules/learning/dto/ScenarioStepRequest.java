package com.exe.buddy_english_be.modules.learning.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ScenarioStepRequest(
        @NotNull(message = "scenarioId is required")
        Long scenarioId,

        @NotNull(message = "stepOrder is required")
        @Min(value = 1, message = "stepOrder must be at least 1")
        Integer stepOrder,

        @Size(max = 1000, message = "buddyMessage must be at most 1000 characters")
        String buddyMessage,

        @Size(max = 100, message = "expectedIntent must be at most 100 characters")
        String expectedIntent,

        @Size(max = 100, message = "expectedEntity must be at most 100 characters")
        String expectedEntity,

        @Size(max = 500, message = "successResponse must be at most 500 characters")
        String successResponse,

        @Size(max = 500, message = "failResponse must be at most 500 characters")
        String failResponse,

        Long nextStepId
) {
}
