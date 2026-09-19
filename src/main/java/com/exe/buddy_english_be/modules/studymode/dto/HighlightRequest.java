package com.exe.buddy_english_be.modules.studymode.dto;

import jakarta.validation.constraints.NotNull;

public record HighlightRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "vocabularyId is required")
        Long vocabularyId,

        /**
         * JSON array string:
         * [{"field":"meaning","start":0,"end":5,"color":"#FFD700"}, ...]
         * Pass null or empty string to clear all highlights.
         */
        String highlightData,

        String userNote
) {}
