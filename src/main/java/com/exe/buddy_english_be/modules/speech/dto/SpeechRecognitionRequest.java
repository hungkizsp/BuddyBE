package com.exe.buddy_english_be.modules.speech.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechRecognitionRequest {
    private Long scenarioId;

    @JsonAlias({"stepId", "stepOrder"})
    private Long stepId;

    private Integer stepOrder;

    private String transcript;
}
