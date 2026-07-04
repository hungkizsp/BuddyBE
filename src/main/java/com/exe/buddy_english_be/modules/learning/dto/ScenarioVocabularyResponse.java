package com.exe.buddy_english_be.modules.learning.dto;

import java.time.LocalDateTime;

public record ScenarioVocabularyResponse(

        Long id,

        Long scenarioId,
        String scenarioTitle,

        Long vocabularyId,

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
