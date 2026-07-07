package com.exe.buddy_english_be.modules.speech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechRecognitionResponse {
    private boolean matched;
    private String transcript;
    private String expectedIntent;
    private String detectedIntent;
    private String expectedEntity;
    private String detectedEntity;
    private String feedback;
}
