package com.exe.buddy_english_be.modules.adventure.dto;

import jakarta.validation.constraints.NotBlank;

public record BuddyPositionDto(
        @NotBlank(message = "left is required")
        String left,

        @NotBlank(message = "top is required")
        String top
) {
}
