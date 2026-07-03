package com.exe.buddy_english_be.modules.progress.service;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.progress.dto.AdventureProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.ChildProgressSummaryResponse;
import com.exe.buddy_english_be.modules.progress.dto.VocabularyProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.WorldProgressResponse;
import com.exe.buddy_english_be.modules.progress.entity.ChildAdventureProgress;
import com.exe.buddy_english_be.modules.progress.entity.ChildVocabularyProgress;
import com.exe.buddy_english_be.modules.progress.entity.ChildWorldProgress;
import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import com.exe.buddy_english_be.modules.progress.repository.ChildAdventureProgressRepository;
import com.exe.buddy_english_be.modules.progress.repository.ChildVocabularyProgressRepository;
import com.exe.buddy_english_be.modules.progress.repository.ChildWorldProgressRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final ChildProfileRepository childProfileRepository;
    private final ChildWorldProgressRepository worldProgressRepository;
    private final ChildAdventureProgressRepository adventureProgressRepository;
    private final ChildVocabularyProgressRepository vocabularyProgressRepository;

    // ─── Summary ─────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ChildProgressSummaryResponse getProgressSummary(Long userId) {
        log.info("Fetching progress summary for userId={}", userId);
        ChildProfile child = resolveChild(userId);

        List<WorldProgressResponse> worldProgress = mapWorldProgress(
                worldProgressRepository.findByChildId(child.getId()));
        List<AdventureProgressResponse> adventureProgress = mapAdventureProgress(
                adventureProgressRepository.findByChildId(child.getId()));
        List<VocabularyProgressResponse> vocabularyProgress = mapVocabularyProgress(
                vocabularyProgressRepository.findByChildIdOrderByLastPracticedDesc(child.getId()));

        long worldsUnlocked = worldProgress.stream()
                .filter(w -> w.status() != ProgressStatus.LOCKED)
                .count();
        long adventuresCompleted = adventureProgress.stream()
                .filter(a -> a.status() == ProgressStatus.COMPLETED)
                .count();

        return ChildProgressSummaryResponse.builder()
                .childId(child.getId())
                .nickname(child.getNickname())
                .level(child.getLevel())
                .xp(child.getXp())
                .coins(child.getCoins())
                .streakDays(child.getStreakDays())
                .totalWordsLearned(vocabularyProgress.size())
                .totalWorldsUnlocked((int) worldsUnlocked)
                .totalAdventuresCompleted((int) adventuresCompleted)
                .worldProgress(worldProgress)
                .adventureProgress(adventureProgress)
                .vocabularyProgress(vocabularyProgress)
                .build();
    }

    // ─── Per-category ─────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<WorldProgressResponse> getWorldProgress(Long userId) {
        log.info("Fetching world progress for userId={}", userId);
        ChildProfile child = resolveChild(userId);
        return mapWorldProgress(worldProgressRepository.findByChildId(child.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdventureProgressResponse> getAdventureProgress(Long userId) {
        log.info("Fetching adventure progress for userId={}", userId);
        ChildProfile child = resolveChild(userId);
        return mapAdventureProgress(adventureProgressRepository.findByChildId(child.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VocabularyProgressResponse> getVocabularyProgress(Long userId) {
        log.info("Fetching vocabulary progress for userId={}", userId);
        ChildProfile child = resolveChild(userId);
        return mapVocabularyProgress(
                vocabularyProgressRepository.findByChildIdOrderByLastPracticedDesc(child.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VocabularyProgressResponse> getDueVocabulary(Long userId) {
        log.info("Fetching due vocabulary for userId={}", userId);
        ChildProfile child = resolveChild(userId);
        return mapVocabularyProgress(
                vocabularyProgressRepository.findByChildIdAndNextReviewAtBefore(
                        child.getId(), LocalDateTime.now()));
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private ChildProfile resolveChild(Long userId) {
        return childProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private List<WorldProgressResponse> mapWorldProgress(List<ChildWorldProgress> list) {
        return list.stream().map(w -> WorldProgressResponse.builder()
                .id(w.getId())
                .worldId(w.getWorld() != null ? w.getWorld().getId() : null)
                .worldName(w.getWorld() != null ? w.getWorld().getName() : null)
                .status(w.getStatus())
                .completionPercentage(w.getCompletionPercentage())
                .lastPlayedAt(w.getLastPlayedAt())
                .unlockedAt(w.getUnlockedAt())
                .build()).toList();
    }

    private List<AdventureProgressResponse> mapAdventureProgress(List<ChildAdventureProgress> list) {
        return list.stream().map(a -> AdventureProgressResponse.builder()
                .id(a.getId())
                .adventureId(a.getAdventure() != null ? a.getAdventure().getId() : null)
                .adventureName(a.getAdventure() != null ? a.getAdventure().getName() : null)
                .status(a.getStatus())
                .score(a.getScore())
                .bestScore(a.getBestScore())
                .attemptCount(a.getAttemptCount())
                .lastPlayedAt(a.getLastPlayedAt())
                .completedAt(a.getCompletedAt())
                .build()).toList();
    }

    private List<VocabularyProgressResponse> mapVocabularyProgress(List<ChildVocabularyProgress> list) {
        return list.stream().map(v -> VocabularyProgressResponse.builder()
                .id(v.getId())
                .vocabularyId(v.getVocabulary() != null ? v.getVocabulary().getId() : null)
                .word(v.getVocabulary() != null ? v.getVocabulary().getWord() : null)
                .meaning(v.getVocabulary() != null ? v.getVocabulary().getMeaning() : null)
                .masteryLevel(v.getMasteryLevel())
                .correctCount(v.getCorrectCount())
                .wrongCount(v.getWrongCount())
                .confidenceScore(v.getConfidenceScore())
                .firstLearnedAt(v.getFirstLearnedAt())
                .nextReviewAt(v.getNextReviewAt())
                .lastPracticed(v.getLastPracticed())
                .build()).toList();
    }
}
