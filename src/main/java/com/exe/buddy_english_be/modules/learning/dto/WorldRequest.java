package com.exe.buddy_english_be.modules.learning.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record WorldRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "description must be at most 500 characters")
        String description,

        @Size(max = 500, message = "thumbnail must be at most 500 characters")
        String thumbnail,

        @Min(value = 0, message = "orderIndex must be at least 0")
        Integer orderIndex,

        Boolean isActive
) {
}
