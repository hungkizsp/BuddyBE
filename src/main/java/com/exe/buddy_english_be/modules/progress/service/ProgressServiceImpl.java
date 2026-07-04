package com.exe.buddy_english_be.modules.progress.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exe.buddy_english_be.modules.learning.entity.Scenario;
import com.exe.buddy_english_be.modules.learning.entity.World;
import com.exe.buddy_english_be.modules.learning.repository.ScenarioRepository;
import com.exe.buddy_english_be.modules.learning.repository.WorldRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.progress.dto.ChildScenarioProgressRequest;
import com.exe.buddy_english_be.modules.progress.dto.ChildScenarioProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.ChildVocabularyProgressRequest;
import com.exe.buddy_english_be.modules.progress.dto.ChildVocabularyProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.ChildWorldProgressRequest;
import com.exe.buddy_english_be.modules.progress.dto.ChildWorldProgressResponse;
import com.exe.buddy_english_be.modules.progress.entity.ChildScenarioProgress;
import com.exe.buddy_english_be.modules.progress.entity.ChildVocabularyProgress;
import com.exe.buddy_english_be.modules.progress.entity.ChildWorldProgress;
import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import com.exe.buddy_english_be.modules.progress.repository.ChildScenarioProgressRepository;
import com.exe.buddy_english_be.modules.progress.repository.ChildVocabularyProgressRepository;
import com.exe.buddy_english_be.modules.progress.repository.ChildWorldProgressRepository;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProgressServiceImpl implements ProgressService {
    private final ChildVocabularyProgressRepository vocabularyProgressRepository;
    private final ChildWorldProgressRepository worldProgressRepository;
    private final ChildScenarioProgressRepository scenarioProgressRepository;
    private final ChildProfileRepository childProfileRepository;
    private final VocabularyRepository vocabularyRepository;
    private final WorldRepository worldRepository;
    private final ScenarioRepository scenarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChildVocabularyProgressResponse> getVocabularyProgressByChildId(Long childId) {
        return vocabularyProgressRepository.findByChildIdOrderByLastPracticedDesc(childId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildVocabularyProgressResponse> getDueVocabularyProgress(Long childId) {
        return vocabularyProgressRepository.findByChildIdAndNextReviewAtBefore(childId, LocalDateTime.now()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChildVocabularyProgressResponse getVocabularyProgressById(Long id) {
        return toResponse(findVocabularyProgress(id));
    }

    @Override
    @Transactional
    public ChildVocabularyProgressResponse createVocabularyProgress(ChildVocabularyProgressRequest request) {
        ChildVocabularyProgress progress = ChildVocabularyProgress.builder()
                .child(findChild(request.childId()))
                .vocabulary(findVocabulary(request.vocabularyId()))
                .masteryLevel(valueOrDefault(request.masteryLevel(), 1))
                .correctCount(valueOrDefault(request.correctCount(), 0))
                .wrongCount(valueOrDefault(request.wrongCount(), 0))
                .confidenceScore(valueOrDefault(request.confidenceScore(), 0.0))
                .firstLearnedAt(request.firstLearnedAt())
                .nextReviewAt(request.nextReviewAt())
                .lastPracticed(request.lastPracticed())
                .build();

        return toResponse(vocabularyProgressRepository.save(progress));
    }

    @Override
    @Transactional
    public ChildVocabularyProgressResponse updateVocabularyProgress(Long id, ChildVocabularyProgressRequest request) {
        ChildVocabularyProgress progress = findVocabularyProgress(id);
        progress.setChild(findChild(request.childId()));
        progress.setVocabulary(findVocabulary(request.vocabularyId()));
        progress.setMasteryLevel(valueOrDefault(request.masteryLevel(), progress.getMasteryLevel()));
        progress.setCorrectCount(valueOrDefault(request.correctCount(), progress.getCorrectCount()));
        progress.setWrongCount(valueOrDefault(request.wrongCount(), progress.getWrongCount()));
        progress.setConfidenceScore(valueOrDefault(request.confidenceScore(), progress.getConfidenceScore()));
        progress.setFirstLearnedAt(request.firstLearnedAt());
        progress.setNextReviewAt(request.nextReviewAt());
        progress.setLastPracticed(request.lastPracticed());

        return toResponse(vocabularyProgressRepository.save(progress));
    }

    @Override
    @Transactional
    public void deleteVocabularyProgress(Long id) {
        vocabularyProgressRepository.delete(findVocabularyProgress(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildWorldProgressResponse> getWorldProgressByChildId(Long childId) {
        return worldProgressRepository.findByChildId(childId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChildWorldProgressResponse getWorldProgressById(Long id) {
        return toResponse(findWorldProgress(id));
    }

    @Override
    @Transactional
    public ChildWorldProgressResponse createWorldProgress(ChildWorldProgressRequest request) {
        ChildWorldProgress progress = ChildWorldProgress.builder()
                .child(findChild(request.childId()))
                .world(findWorld(request.worldId()))
                .status(valueOrDefault(request.status(), ProgressStatus.LOCKED))
                .completionPercentage(valueOrDefault(request.completionPercentage(), 0))
                .lastPlayedAt(request.lastPlayedAt())
                .unlockedAt(request.unlockedAt())
                .build();

        return toResponse(worldProgressRepository.save(progress));
    }

    @Override
    @Transactional
    public ChildWorldProgressResponse updateWorldProgress(Long id, ChildWorldProgressRequest request) {
        ChildWorldProgress progress = findWorldProgress(id);
        progress.setChild(findChild(request.childId()));
        progress.setWorld(findWorld(request.worldId()));
        progress.setStatus(valueOrDefault(request.status(), progress.getStatus()));
        progress.setCompletionPercentage(valueOrDefault(request.completionPercentage(), progress.getCompletionPercentage()));
        progress.setLastPlayedAt(request.lastPlayedAt());
        progress.setUnlockedAt(request.unlockedAt());

        return toResponse(worldProgressRepository.save(progress));
    }

    @Override
    @Transactional
    public void deleteWorldProgress(Long id) {
        worldProgressRepository.delete(findWorldProgress(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildScenarioProgressResponse> getScenarioProgressByChildId(Long childId) {
        return scenarioProgressRepository.findByChildId(childId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChildScenarioProgressResponse getScenarioProgressById(Long id) {
        return toResponse(findScenarioProgress(id));
    }

    @Override
    @Transactional
    public ChildScenarioProgressResponse createScenarioProgress(ChildScenarioProgressRequest request) {
        ChildScenarioProgress progress = ChildScenarioProgress.builder()
                .child(findChild(request.childId()))
                .scenario(findScenario(request.scenarioId()))
                .status(valueOrDefault(request.status(), ProgressStatus.LOCKED))
                .score(valueOrDefault(request.score(), 0))
                .bestScore(valueOrDefault(request.bestScore(), 0))
                .attemptCount(valueOrDefault(request.attemptCount(), 0))
                .lastPlayedAt(request.lastPlayedAt())
                .completedAt(request.completedAt())
                .build();

        return toResponse(scenarioProgressRepository.save(progress));
    }

    @Override
    @Transactional
    public ChildScenarioProgressResponse updateScenarioProgress(Long id, ChildScenarioProgressRequest request) {
        ChildScenarioProgress progress = findScenarioProgress(id);
        progress.setChild(findChild(request.childId()));
        progress.setScenario(findScenario(request.scenarioId()));
        progress.setStatus(valueOrDefault(request.status(), progress.getStatus()));
        progress.setScore(valueOrDefault(request.score(), progress.getScore()));
        progress.setBestScore(valueOrDefault(request.bestScore(), progress.getBestScore()));
        progress.setAttemptCount(valueOrDefault(request.attemptCount(), progress.getAttemptCount()));
        progress.setLastPlayedAt(request.lastPlayedAt());
        progress.setCompletedAt(request.completedAt());

        return toResponse(scenarioProgressRepository.save(progress));
    }

    @Override
    @Transactional
    public void deleteScenarioProgress(Long id) {
        scenarioProgressRepository.delete(findScenarioProgress(id));
    }

    private ChildVocabularyProgress findVocabularyProgress(Long id) {
        return vocabularyProgressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_NOT_FOUND));
    }

    private ChildWorldProgress findWorldProgress(Long id) {
        return worldProgressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_NOT_FOUND));
    }

    private ChildScenarioProgress findScenarioProgress(Long id) {
        return scenarioProgressRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROGRESS_NOT_FOUND));
    }

    private ChildProfile findChild(Long id) {
        return childProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
    }

    private Vocabulary findVocabulary(Long id) {
        return vocabularyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.VOCABULARY_NOT_FOUND));
    }

    private World findWorld(Long id) {
        return worldRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.WORLD_NOT_FOUND));
    }

    private Scenario findScenario(Long id) {
        return scenarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SCENARIO_NOT_FOUND));
    }

    private ChildVocabularyProgressResponse toResponse(ChildVocabularyProgress progress) {
        return new ChildVocabularyProgressResponse(
                progress.getId(),
                progress.getChild().getId(),
                progress.getVocabulary().getId(),
                progress.getVocabulary().getWord(),
                progress.getMasteryLevel(),
                progress.getCorrectCount(),
                progress.getWrongCount(),
                progress.getConfidenceScore(),
                progress.getFirstLearnedAt(),
                progress.getNextReviewAt(),
                progress.getLastPracticed(),
                progress.getCreatedAt(),
                progress.getUpdatedAt()
        );
    }

    private ChildWorldProgressResponse toResponse(ChildWorldProgress progress) {
        return new ChildWorldProgressResponse(
                progress.getId(),
                progress.getChild().getId(),
                progress.getWorld().getId(),
                progress.getWorld().getName(),
                progress.getStatus(),
                progress.getCompletionPercentage(),
                progress.getLastPlayedAt(),
                progress.getUnlockedAt(),
                progress.getCreatedAt(),
                progress.getUpdatedAt()
        );
    }

    private ChildScenarioProgressResponse toResponse(ChildScenarioProgress progress) {
        return new ChildScenarioProgressResponse(
                progress.getId(),
                progress.getChild().getId(),
                progress.getScenario().getId(),
                progress.getScenario().getTitle(),
                progress.getStatus(),
                progress.getScore(),
                progress.getBestScore(),
                progress.getAttemptCount(),
                progress.getLastPlayedAt(),
                progress.getCompletedAt(),
                progress.getCreatedAt(),
                progress.getUpdatedAt()
        );
    }

    private <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
