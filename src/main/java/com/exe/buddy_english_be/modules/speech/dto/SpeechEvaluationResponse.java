package com.exe.buddy_english_be.modules.speech.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpeechEvaluationResponse {
    // private boolean matched;
    // private String transcript;
    // private String expectedIntent;
    // private String detectedIntent;
    // private String expectedEntity;
    // private String detectedEntity;
    // private String feedback;

    private String overallFeedback;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> improvementTips;
    private List<WordFeedback> wordFeedback;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WordFeedback {
        private String word;
        private String problem;
        private String tip;
    }
}
