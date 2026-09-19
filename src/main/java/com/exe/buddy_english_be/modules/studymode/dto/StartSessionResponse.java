package com.exe.buddy_english_be.modules.studymode.dto;

import com.exe.buddy_english_be.modules.studymode.enums.StudyMode;
import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyResponse;

import java.util.List;

public record StartSessionResponse(
        Long sessionId,
        Long categoryId,
        String categoryName,
        StudyMode mode,
        List<VocabularyResponse> vocabularies
) {}
