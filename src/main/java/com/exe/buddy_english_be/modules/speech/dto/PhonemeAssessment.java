package com.exe.buddy_english_be.modules.speech.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhonemeAssessment {
    private String phoneme;
    private Double accuracy;
}
