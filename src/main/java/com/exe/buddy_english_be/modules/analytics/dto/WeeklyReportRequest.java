package com.exe.buddy_english_be.modules.analytics.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record WeeklyReportRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "weekStart is required")
        LocalDate weekStart,

        @NotNull(message = "weekEnd is required")
        LocalDate weekEnd
) {
}
