package com.exe.buddy_english_be.modules.learning.dto;

import java.time.LocalDateTime;

public record WorldResponse(
        Long id,
        String name,
        String description,
        String thumbnail,
        Integer orderIndex,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
