package com.exe.buddy_english_be.modules.vocabulary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VocabularyRequest(
        Long categoryId,

        @NotBlank(message = "word is required")
        @Size(max = 100, message = "word must be at most 100 characters")
        String word,

        @Size(max = 100, message = "phonetic must be at most 100 characters")
        String phonetic,

        @Size(max = 500, message = "meaning must be at most 500 characters")
        String meaning,

        @Size(max = 500, message = "exampleSentence must be at most 500 characters")
        String exampleSentence,

        @Size(max = 500, message = "imageUrl must be at most 500 characters")
        String imageUrl,

        @Size(max = 500, message = "audioUrl must be at most 500 characters")
        String audioUrl,

        @Size(max = 20, message = "difficulty must be at most 20 characters")
        String difficulty
) {
}
