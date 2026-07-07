package com.exe.buddy_english_be.modules.speech.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exe.buddy_english_be.modules.speech.dto.SpeechRecognitionRequest;
import com.exe.buddy_english_be.modules.speech.dto.SpeechRecognitionResponse;
import com.exe.buddy_english_be.modules.speech.service.SpeechRecognitionService;
import com.exe.buddy_english_be.shared.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController("modulesSpeechRecognitionController")
@RequestMapping("/api/speech")
@RequiredArgsConstructor
@Slf4j
public class SpeechRecognitionController {

    private final SpeechRecognitionService speechRecognitionService;

    @PostMapping("/recognize")
    public ResponseEntity<ApiResponse<SpeechRecognitionResponse>> recognize(
            Authentication authentication,
            @Valid @RequestBody SpeechRecognitionRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        SpeechRecognitionResponse response = speechRecognitionService.recognize(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Speech recognized successfully", response));
    }
}
