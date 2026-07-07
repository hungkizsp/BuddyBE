package com.exe.buddy_english_be.modules.speech.service;

import com.exe.buddy_english_be.modules.speech.dto.SpeechRecognitionRequest;
import com.exe.buddy_english_be.modules.speech.dto.SpeechRecognitionResponse;

public interface SpeechRecognitionService {
    SpeechRecognitionResponse recognize(Long userId, SpeechRecognitionRequest request);
}
