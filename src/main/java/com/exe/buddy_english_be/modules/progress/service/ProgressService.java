package com.exe.buddy_english_be.modules.progress.service;

import com.exe.buddy_english_be.modules.progress.dto.AdventureProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.ChildProgressSummaryResponse;
import com.exe.buddy_english_be.modules.progress.dto.VocabularyProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.WorldProgressResponse;

import java.util.List;

public interface ProgressService {

    /** Full summary for the authenticated child (worlds + adventures + vocabulary). */
    ChildProgressSummaryResponse getProgressSummary(Long userId);

    /** World-level progress for the child. */
    List<WorldProgressResponse> getWorldProgress(Long userId);

    /** Adventure-level progress for the child. */
    List<AdventureProgressResponse> getAdventureProgress(Long userId);

    /** Vocabulary practice progress for the child. */
    List<VocabularyProgressResponse> getVocabularyProgress(Long userId);

    /** Words whose next review is overdue (spaced repetition queue). */
    List<VocabularyProgressResponse> getDueVocabulary(Long userId);
}
