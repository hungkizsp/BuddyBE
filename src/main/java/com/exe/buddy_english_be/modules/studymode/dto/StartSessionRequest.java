package com.exe.buddy_english_be.modules.studymode.dto;

import com.exe.buddy_english_be.modules.studymode.enums.StudyMode;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record StartSessionRequest(
        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "categoryId is required")
        Long categoryId,

        @NotNull(message = "mode is required")
        StudyMode mode,

        List<Long> vocabIds
) {}
