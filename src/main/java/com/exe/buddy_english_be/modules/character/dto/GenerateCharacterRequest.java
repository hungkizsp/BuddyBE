package com.exe.buddy_english_be.modules.character.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenerateCharacterRequest(

        @NotBlank(message = "Prompt is required")
        @Size(max = 500, message = "Prompt must not exceed 500 characters")
        String prompt,

        @NotBlank(message = "Art style is required")
        String artStyle,

        @Size(max = 100, message = "Character name must not exceed 100 characters")
        String characterName
) {}
