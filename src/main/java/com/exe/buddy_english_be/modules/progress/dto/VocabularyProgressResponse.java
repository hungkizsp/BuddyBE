package com.exe.buddy_english_be.modules.progress.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record VocabularyProgressResponse(
        Long id,
        Long vocabularyId,
        String word,
        String meaning,
        Integer masteryLevel,
        Integer correctCount,
        Integer wrongCount,
        Double confidenceScore,
        LocalDateTime firstLearnedAt,
        LocalDateTime nextReviewAt,
        LocalDateTime lastPracticed
) {}
