package com.exe.buddy_english_be.modules.studymode.controller;

import com.exe.buddy_english_be.modules.studymode.dto.SessionResultResponse;
import com.exe.buddy_english_be.modules.studymode.dto.StartSessionRequest;
import com.exe.buddy_english_be.modules.studymode.dto.StartSessionResponse;
import com.exe.buddy_english_be.modules.studymode.dto.SubmitAnswerRequest;
import com.exe.buddy_english_be.modules.studymode.service.StudyModeService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study-sessions")
@RequiredArgsConstructor
public class StudyModeController {

    private final StudyModeService studyModeService;

    /**
     * POST /api/study-sessions/start
     * Starts a new study session, returns shuffled vocab list.
     */
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<StartSessionResponse>> startSession(
            @Valid @RequestBody StartSessionRequest request) {
        StartSessionResponse response = studyModeService.startSession(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Session started successfully", response));
    }

    /**
     * POST /api/study-sessions/{id}/answer
     * Submit an answer for one vocabulary item; updates spaced-repetition progress.
     */
    @PostMapping("/{id}/answer")
    public ResponseEntity<ApiResponse<Void>> submitAnswer(
            @PathVariable Long id,
            @Valid @RequestBody SubmitAnswerRequest request) {
        studyModeService.submitAnswer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Answer recorded", null));
    }

    /**
     * POST /api/study-sessions/{id}/finish
     * Finishes the session and returns the result (score, weak words, duration).
     */
    @PostMapping("/{id}/finish")
    public ResponseEntity<ApiResponse<SessionResultResponse>> finishSession(
            @PathVariable Long id) {
        SessionResultResponse result = studyModeService.finishSession(id);
        return ResponseEntity.ok(ApiResponse.success("Session completed", result));
    }
}
