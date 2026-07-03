package com.exe.buddy_english_be.modules.progress.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ChildProgressSummaryResponse(
        Long childId,
        String nickname,
        Integer level,
        Integer xp,
        Integer coins,
        Integer streakDays,
        int totalWordsLearned,
        int totalWorldsUnlocked,
        int totalAdventuresCompleted,
        List<WorldProgressResponse> worldProgress,
        List<AdventureProgressResponse> adventureProgress,
        List<VocabularyProgressResponse> vocabularyProgress
) {}
