package com.exe.buddy_english_be.modules.conversation.dto;

import lombok.Builder;

@Builder
public record ChatResponse(
        String reply,
        String summary,
        String vocabularyWord,
        String vocabularyMeaning,
        String vocabularyCategory,
        boolean allLearnedVocabularyAsked
) {
}
