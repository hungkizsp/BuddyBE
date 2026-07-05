package com.exe.buddy_english_be.modules.speech.service;

import com.exe.buddy_english_be.modules.speech.dto.SpeechEvaluationRequest;
import com.exe.buddy_english_be.modules.speech.dto.SpeechEvaluationResponse;

public interface SpeechEvaluationService {
    SpeechEvaluationResponse evaluate(Long userId, SpeechEvaluationRequest request);
}
