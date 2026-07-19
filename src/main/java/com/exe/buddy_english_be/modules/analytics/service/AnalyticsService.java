package com.exe.buddy_english_be.modules.analytics.service;

import com.exe.buddy_english_be.modules.analytics.dto.LearningSessionRequest;
import com.exe.buddy_english_be.modules.analytics.dto.LearningSessionResponse;
import com.exe.buddy_english_be.modules.analytics.dto.WeeklyReportRequest;
import com.exe.buddy_english_be.modules.analytics.dto.WeeklyReportResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalyticsService {

    LearningSessionResponse startSession(LearningSessionRequest request);

    LearningSessionResponse endSession(Long id, LocalDateTime endTime);

    LearningSessionResponse getSessionById(Long id);

    List<LearningSessionResponse> getSessionsByChild(Long childId);

    WeeklyReportResponse generateWeeklyReport(WeeklyReportRequest request);

    List<WeeklyReportResponse> getWeeklyReportsByChild(Long childId);

    WeeklyReportResponse getLatestWeeklyReport(Long childId);
}
