package com.exe.buddy_english_be.modules.buddy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateChildMemoryRequest(
        @NotNull(message = "Child ID is required")
        Long childId,
        @NotBlank(message = "Memory type is required")
        String memoryType,
        @NotBlank(message = "Memory key is required")
        String memoryKey,
        String memoryValue,
        Double confidence,
        String source,
        Integer importance,
        Boolean isActive,
        LocalDateTime lastUsedAt
) {}
