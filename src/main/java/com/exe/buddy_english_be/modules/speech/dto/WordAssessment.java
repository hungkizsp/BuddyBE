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
public class WordAssessment {
    private String word;
    private Double accuracy;
    private String errorType;
    private List<PhonemeAssessment> phonemes;
}
