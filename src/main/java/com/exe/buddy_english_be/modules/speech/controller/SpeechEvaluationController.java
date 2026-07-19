package com.exe.buddy_english_be.modules.speech.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exe.buddy_english_be.modules.speech.dto.SpeechEvaluationRequest;
import com.exe.buddy_english_be.modules.speech.dto.SpeechEvaluationResponse;
import com.exe.buddy_english_be.modules.speech.service.SpeechEvaluationService;
import com.exe.buddy_english_be.shared.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController("modulesSpeechEvaluationController")
@RequestMapping("/api/speech")
@RequiredArgsConstructor
public class SpeechEvaluationController {

    private final SpeechEvaluationService speechEvaluationService;

    @PostMapping("/evaluate")
    public ResponseEntity<ApiResponse<SpeechEvaluationResponse>> evaluate(
            Authentication authentication,
            @Valid @RequestBody SpeechEvaluationRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        SpeechEvaluationResponse response = speechEvaluationService.evaluate(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Pronunciation evaluated successfully", response));
    }
}
