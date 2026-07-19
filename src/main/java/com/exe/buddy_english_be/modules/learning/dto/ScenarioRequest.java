package com.exe.buddy_english_be.modules.learning.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ScenarioRequest(
        @NotNull(message = "worldId is required")
        Long worldId,

        @NotBlank(message = "title is required")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @Size(max = 500, message = "description must be at most 500 characters")
        String description,

        @Size(max = 50, message = "scenarioType must be at most 50 characters")
        String scenarioType,

        @Size(max = 100, message = "expectedIntent must be at most 100 characters")
        String expectedIntent,

        @Size(max = 20, message = "difficulty must be at most 20 characters")
        String difficulty,

        @Min(value = 0, message = "orderIndex must be at least 0")
        Integer orderIndex
) {
}
