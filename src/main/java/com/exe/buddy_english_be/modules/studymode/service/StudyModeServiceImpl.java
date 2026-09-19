package com.exe.buddy_english_be.modules.studymode.service;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.progress.entity.ChildVocabularyProgress;
import com.exe.buddy_english_be.modules.progress.repository.ChildVocabularyProgressRepository;
import com.exe.buddy_english_be.modules.studymode.dto.SessionResultResponse;
import com.exe.buddy_english_be.modules.studymode.dto.StartSessionRequest;
import com.exe.buddy_english_be.modules.studymode.dto.StartSessionResponse;
import com.exe.buddy_english_be.modules.studymode.dto.SubmitAnswerRequest;
import com.exe.buddy_english_be.modules.studymode.entity.StudySession;
import com.exe.buddy_english_be.modules.studymode.repository.StudySessionRepository;
import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyResponse;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.entity.VocabularyCategory;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyCategoryRepository;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyModeServiceImpl implements StudyModeService {

    private final StudySessionRepository studySessionRepository;
    private final ChildProfileRepository childProfileRepository;
    private final VocabularyCategoryRepository categoryRepository;
    private final VocabularyRepository vocabularyRepository;
    private final ChildVocabularyProgressRepository vocabProgressRepository;

    // Keeps track of wrong vocab IDs per session (in-memory, suitable for localhost)
    private final Map<Long, Set<Long>> wrongVocabCache = new HashMap<>();

    @Override
    @Transactional
    public StartSessionResponse startSession(StartSessionRequest request) {
        ChildProfile child = childProfileRepository.findById(request.childId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
        VocabularyCategory category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.VOCABULARY_CATEGORY_NOT_FOUND));

        List<Vocabulary> vocabs;
        if (request.vocabIds() != null && !request.vocabIds().isEmpty()) {
            vocabs = vocabularyRepository.findAllById(request.vocabIds());
        } else {
            vocabs = vocabularyRepository.findByCategoryId(request.categoryId());
        }

        if (vocabs.isEmpty()) {
            throw new BusinessException(ErrorCode.VOCABULARY_NOT_FOUND);
        }

        // Shuffle for randomness
        Collections.shuffle(vocabs);

        StudySession session = StudySession.builder()
                .child(child)
                .category(category)
                .mode(request.mode())
                .totalWords(vocabs.size())
                .build();
        session = studySessionRepository.save(session);

        // Init wrong vocab tracking for this session
        wrongVocabCache.put(session.getId(), new HashSet<>());

        List<VocabularyResponse> vocabResponses = vocabs.stream()
                .map(this::toVocabResponse)
                .collect(Collectors.toList());

        return new StartSessionResponse(
                session.getId(),
                category.getId(),
                category.getName(),
                request.mode(),
                vocabResponses
        );
    }

    @Override
    @Transactional
    public void submitAnswer(Long sessionId, SubmitAnswerRequest request) {
        StudySession session = findSession(sessionId);
        ChildProfile child = session.getChild();
        Vocabulary vocab = vocabularyRepository.findById(request.vocabularyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.VOCABULARY_NOT_FOUND));

        // Update correct/wrong counters on session
        if (Boolean.TRUE.equals(request.isCorrect())) {
            session.setCorrectCount(session.getCorrectCount() + 1);
        } else {
            session.setWrongCount(session.getWrongCount() + 1);
            wrongVocabCache.computeIfAbsent(sessionId, k -> new HashSet<>())
                    .add(vocab.getId());
        }
        studySessionRepository.save(session);

        // Update spaced-repetition progress
        updateVocabularyProgress(child, vocab, request.isCorrect());
    }

    @Override
    @Transactional
    public SessionResultResponse finishSession(Long sessionId) {
        StudySession session = findSession(sessionId);

        // Calculate score
        int total = session.getTotalWords();
        int correct = session.getCorrectCount();
        int score = total > 0 ? Math.round((correct * 100f) / total) : 0;

        // Duration = seconds since session was created
        int duration = (int) java.time.Duration.between(
                session.getCreatedAt(), LocalDateTime.now()).getSeconds();

        session.setScore(score);
        session.setDurationSeconds(duration);
        session.setCompletedAt(LocalDateTime.now());
        session.setIsCompleted(true);
        studySessionRepository.save(session);

        // Build weak words list
        Set<Long> wrongIds = wrongVocabCache.getOrDefault(sessionId, Collections.emptySet());
        List<VocabularyResponse> weakWords = wrongIds.stream()
                .map(id -> vocabularyRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .map(this::toVocabResponse)
                .collect(Collectors.toList());

        // Clean up cache
        wrongVocabCache.remove(sessionId);

        return new SessionResultResponse(
                session.getId(),
                session.getTotalWords(),
                session.getCorrectCount(),
                session.getWrongCount(),
                score,
                duration,
                weakWords
        );
    }

    /**
     * Spaced-repetition logic:
     * - Correct: increase masteryLevel (max 5), increase confidenceScore, schedule next review further out
     * - Wrong: decrease masteryLevel (min 1), decrease confidenceScore, schedule next review soon
     */
    private void updateVocabularyProgress(ChildProfile child, Vocabulary vocab, Boolean isCorrect) {
        ChildVocabularyProgress progress = vocabProgressRepository
                .findByChildIdAndVocabularyId(child.getId(), vocab.getId())
                .orElseGet(() -> ChildVocabularyProgress.builder()
                        .child(child)
                        .vocabulary(vocab)
                        .firstLearnedAt(LocalDateTime.now())
                        .build());

        if (Boolean.TRUE.equals(isCorrect)) {
            progress.setCorrectCount(progress.getCorrectCount() + 1);
            progress.setMasteryLevel(Math.min(progress.getMasteryLevel() + 1, 5));
            progress.setConfidenceScore(Math.min(progress.getConfidenceScore() + 0.2, 1.0));
        } else {
            progress.setWrongCount(progress.getWrongCount() + 1);
            progress.setMasteryLevel(Math.max(progress.getMasteryLevel() - 1, 1));
            progress.setConfidenceScore(Math.max(progress.getConfidenceScore() - 0.15, 0.0));
        }

        // Spaced repetition: next review = now + (masteryLevel * 2) days
        progress.setNextReviewAt(LocalDateTime.now().plusDays(progress.getMasteryLevel() * 2L));
        progress.setLastPracticed(LocalDateTime.now());

        vocabProgressRepository.save(progress);
    }

    private StudySession findSession(Long sessionId) {
        return studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.STUDY_SESSION_NOT_FOUND));
    }

    private VocabularyResponse toVocabResponse(Vocabulary v) {
        VocabularyCategory cat = v.getCategory();
        return new VocabularyResponse(
                v.getId(),
                cat != null ? cat.getId() : null,
                cat != null ? cat.getName() : null,
                v.getWord(),
                v.getPhonetic(),
                v.getMeaning(),
                v.getExampleSentence(),
                v.getImageUrl(),
                v.getAudioUrl(),
                v.getDifficulty(),
                v.getCreatedAt(),
                v.getUpdatedAt()
        );
    }
}
