package com.exe.buddy_english_be.modules.studymode.dto;

import java.time.LocalDateTime;

public record HighlightResponse(
        Long id,
        Long childId,
        Long vocabularyId,
        String highlightData,
        String userNote,
        LocalDateTime updatedAt
) {}
