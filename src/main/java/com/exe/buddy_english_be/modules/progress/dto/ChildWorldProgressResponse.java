package com.exe.buddy_english_be.modules.progress.dto;

import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;

import java.time.LocalDateTime;

public record ChildWorldProgressResponse(
        Long id,
        Long childId,
        Long worldId,
        String worldName,
        ProgressStatus status,
        Integer completionPercentage,
        LocalDateTime lastPlayedAt,
        LocalDateTime unlockedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
