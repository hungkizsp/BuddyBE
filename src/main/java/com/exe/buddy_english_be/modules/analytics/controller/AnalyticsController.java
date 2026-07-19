package com.exe.buddy_english_be.modules.analytics.controller;

import com.exe.buddy_english_be.modules.analytics.dto.LearningSessionRequest;
import com.exe.buddy_english_be.modules.analytics.dto.LearningSessionResponse;
import com.exe.buddy_english_be.modules.analytics.dto.WeeklyReportRequest;
import com.exe.buddy_english_be.modules.analytics.dto.WeeklyReportResponse;
import com.exe.buddy_english_be.modules.analytics.service.AnalyticsService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController("modulesAnalyticsController")
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    // ---- Learning Sessions ----

    @PostMapping("/sessions")
    public ResponseEntity<ApiResponse<LearningSessionResponse>> startSession(
            @Valid @RequestBody LearningSessionRequest request) {
        LearningSessionResponse response = analyticsService.startSession(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Session started successfully", response));
    }

    @PutMapping("/sessions/{id}/end")
    public ResponseEntity<ApiResponse<LearningSessionResponse>> endSession(
            @PathVariable Long id,
            @RequestParam(required = false) LocalDateTime endTime) {
        LearningSessionResponse response = analyticsService.endSession(id, endTime);
        return ResponseEntity.ok(ApiResponse.success("Session ended successfully", response));
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<ApiResponse<LearningSessionResponse>> getSessionById(@PathVariable Long id) {
        LearningSessionResponse response = analyticsService.getSessionById(id);
        return ResponseEntity.ok(ApiResponse.success("Session fetched successfully", response));
    }

    @GetMapping("/sessions")
    public ResponseEntity<ApiResponse<List<LearningSessionResponse>>> getSessionsByChild(
            @RequestParam Long childId) {
        List<LearningSessionResponse> response = analyticsService.getSessionsByChild(childId);
        return ResponseEntity.ok(ApiResponse.success("Sessions fetched successfully", response));
    }

    // ---- Weekly Reports ----

    @PostMapping("/weekly-reports/generate")
    public ResponseEntity<ApiResponse<WeeklyReportResponse>> generateWeeklyReport(
            @Valid @RequestBody WeeklyReportRequest request) {
        WeeklyReportResponse response = analyticsService.generateWeeklyReport(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Weekly report generated successfully", response));
    }

    @GetMapping("/weekly-reports")
    public ResponseEntity<ApiResponse<List<WeeklyReportResponse>>> getWeeklyReportsByChild(
            @RequestParam Long childId) {
        List<WeeklyReportResponse> response = analyticsService.getWeeklyReportsByChild(childId);
        return ResponseEntity.ok(ApiResponse.success("Weekly reports fetched successfully", response));
    }

    @GetMapping("/weekly-reports/latest")
    public ResponseEntity<ApiResponse<WeeklyReportResponse>> getLatestWeeklyReport(
            @RequestParam Long childId) {
        WeeklyReportResponse response = analyticsService.getLatestWeeklyReport(childId);
        return ResponseEntity.ok(ApiResponse.success("Latest weekly report fetched successfully", response));
    }
}
