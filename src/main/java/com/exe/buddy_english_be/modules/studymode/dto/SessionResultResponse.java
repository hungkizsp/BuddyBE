package com.exe.buddy_english_be.modules.studymode.dto;

import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyResponse;

import java.util.List;

public record SessionResultResponse(
        Long sessionId,
        Integer totalWords,
        Integer correctCount,
        Integer wrongCount,
        Integer score,
        Integer durationSeconds,
        /** Vocabularies the child answered incorrectly — suggested for review */
        List<VocabularyResponse> weakWords
) {}
