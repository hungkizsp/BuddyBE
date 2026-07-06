package com.exe.buddy_english_be.modules.speech.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechEvaluationRequest {
    @NotBlank(message = "Expected sentence is required")
    private String expectedSentence;

    @NotNull(message = "Assessment is required")
    @Valid
    private AzureSpeechAssessment assessment;
}
