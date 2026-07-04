package com.exe.buddy_english_be.modules.progress.dto;

import java.time.LocalDateTime;

public record ChildVocabularyProgressResponse(
        Long id,
        Long childId,
        Long vocabularyId,
        String word,
        Integer masteryLevel,
        Integer correctCount,
        Integer wrongCount,
        Double confidenceScore,
        LocalDateTime firstLearnedAt,
        LocalDateTime nextReviewAt,
        LocalDateTime lastPracticed,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
