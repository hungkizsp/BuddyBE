package com.exe.buddy_english_be.modules.buddy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateChildCustomizationRequest(
        @NotNull(message = "Child ID is required")
        Long childId,
        @NotNull(message = "Customization ID is required")
        Long customizationId,
        Boolean equipped,
        LocalDateTime obtainedAt
) {}
