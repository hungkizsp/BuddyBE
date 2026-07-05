package com.exe.buddy_english_be.modules.analytics.service;

import com.exe.buddy_english_be.modules.analytics.dto.LearningSessionRequest;
import com.exe.buddy_english_be.modules.analytics.dto.LearningSessionResponse;
import com.exe.buddy_english_be.modules.analytics.dto.WeeklyReportRequest;
import com.exe.buddy_english_be.modules.analytics.dto.WeeklyReportResponse;
import com.exe.buddy_english_be.modules.analytics.entity.LearningSession;
import com.exe.buddy_english_be.modules.analytics.entity.WeeklyReport;
import com.exe.buddy_english_be.modules.analytics.repository.LearningSessionRepository;
import com.exe.buddy_english_be.modules.analytics.repository.WeeklyReportRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final LearningSessionRepository learningSessionRepository;
    private final WeeklyReportRepository weeklyReportRepository;
    private final ChildProfileRepository childProfileRepository;

    public AnalyticsServiceImpl(
            LearningSessionRepository learningSessionRepository,
            WeeklyReportRepository weeklyReportRepository,
            ChildProfileRepository childProfileRepository) {
        this.learningSessionRepository = learningSessionRepository;
        this.weeklyReportRepository = weeklyReportRepository;
        this.childProfileRepository = childProfileRepository;
    }

    @Override
    @Transactional
    public LearningSessionResponse startSession(LearningSessionRequest request) {
        ChildProfile child = findChild(request.childId());
        LocalDateTime startTime = request.startTime() != null ? request.startTime() : LocalDateTime.now();

        LearningSession session = LearningSession.builder()
                .child(child)
                .startTime(startTime)
                .endTime(request.endTime())
                .sessionType(request.sessionType())
                .totalWordsSpoken(valueOrDefault(request.totalWordsSpoken(), 0))
                .totalAttempts(valueOrDefault(request.totalAttempts(), 0))
                .build();

        if (request.endTime() != null) {
            long seconds = java.time.Duration.between(startTime, request.endTime()).getSeconds();
            session.setDurationSeconds(seconds);
        }

        return toResponse(learningSessionRepository.save(session));
    }

    @Override
    @Transactional
    public LearningSessionResponse endSession(Long id, LocalDateTime endTime) {
        LearningSession session = findSession(id);
        LocalDateTime end = endTime != null ? endTime : LocalDateTime.now();
        session.setEndTime(end);
        if (session.getStartTime() != null) {
            long seconds = java.time.Duration.between(session.getStartTime(), end).getSeconds();
            session.setDurationSeconds(seconds);
        }
        return toResponse(learningSessionRepository.save(session));
    }

    @Override
    @Transactional(readOnly = true)
    public LearningSessionResponse getSessionById(Long id) {
        return toResponse(findSession(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningSessionResponse> getSessionsByChild(Long childId) {
        return learningSessionRepository.findByChildIdOrderByStartTimeDesc(childId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public WeeklyReportResponse generateWeeklyReport(WeeklyReportRequest request) {
        ChildProfile child = findChild(request.childId());

        // Aggregate from learning sessions in the given week
        List<LearningSession> sessions = learningSessionRepository
                .findByChildIdOrderByStartTimeDesc(request.childId())
                .stream()
                .filter(s -> s.getStartTime() != null
                        && !s.getStartTime().toLocalDate().isBefore(request.weekStart())
                        && !s.getStartTime().toLocalDate().isAfter(request.weekEnd()))
                .toList();

        int totalMinutes = sessions.stream()
                .mapToInt(s -> s.getDurationSeconds() != null ? (int) (s.getDurationSeconds() / 60) : 0)
                .sum();
        int totalWords = sessions.stream()
                .mapToInt(s -> s.getTotalWordsSpoken() != null ? s.getTotalWordsSpoken() : 0)
                .sum();
        int totalAttempts = sessions.stream()
                .mapToInt(s -> s.getTotalAttempts() != null ? s.getTotalAttempts() : 0)
                .sum();

        WeeklyReport report = WeeklyReport.builder()
                .child(child)
                .weekStart(request.weekStart())
                .weekEnd(request.weekEnd())
                .learningMinutes(totalMinutes)
                .wordsLearned(totalWords)
                .speakingAttempts(totalAttempts)
                .completedScenarios(0)
                .generatedAt(LocalDateTime.now())
                .build();

        return toResponse(weeklyReportRepository.save(report));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WeeklyReportResponse> getWeeklyReportsByChild(Long childId) {
        return weeklyReportRepository.findByChildId(childId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WeeklyReportResponse getLatestWeeklyReport(Long childId) {
        return weeklyReportRepository.findTopByChildIdOrderByWeekStartDesc(childId)
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException(ErrorCode.WEEKLY_REPORT_NOT_FOUND));
    }

    // ---- helpers ----

    private LearningSession findSession(Long id) {
        return learningSessionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.LEARNING_SESSION_NOT_FOUND));
    }

    private ChildProfile findChild(Long id) {
        return childProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
    }

    private LearningSessionResponse toResponse(LearningSession s) {
        return new LearningSessionResponse(
                s.getId(),
                s.getChild().getId(),
                s.getStartTime(),
                s.getEndTime(),
                s.getDurationSeconds(),
                s.getSessionType(),
                s.getTotalWordsSpoken(),
                s.getTotalAttempts(),
                s.getCreatedAt()
        );
    }

    private WeeklyReportResponse toResponse(WeeklyReport r) {
        return new WeeklyReportResponse(
                r.getId(),
                r.getChild().getId(),
                r.getWeekStart(),
                r.getWeekEnd(),
                r.getLearningMinutes(),
                r.getWordsLearned(),
                r.getSpeakingAttempts(),
                r.getCompletedScenarios(),
                r.getGeneratedAt(),
                r.getCreatedAt()
        );
    }

    private <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
