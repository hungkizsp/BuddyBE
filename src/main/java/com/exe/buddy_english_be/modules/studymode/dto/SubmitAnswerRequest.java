package com.exe.buddy_english_be.modules.studymode.dto;

import jakarta.validation.constraints.NotNull;

public record SubmitAnswerRequest(
        @NotNull(message = "vocabularyId is required")
        Long vocabularyId,

        @NotNull(message = "isCorrect is required")
        Boolean isCorrect,

        /** Response time in milliseconds; used for adaptive difficulty */
        Integer responseTimeMs
) {}
