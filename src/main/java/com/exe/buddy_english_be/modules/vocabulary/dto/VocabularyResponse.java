package com.exe.buddy_english_be.modules.vocabulary.dto;

import java.time.LocalDateTime;

public record VocabularyResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String word,
        String phonetic,
        String meaning,
        String exampleSentence,
        String imageUrl,
        String audioUrl,
        String difficulty,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
