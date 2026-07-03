package com.exe.buddy_english_be.modules.progress.dto;

import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorldProgressResponse(
        Long id,
        Long worldId,
        String worldName,
        ProgressStatus status,
        Integer completionPercentage,
        LocalDateTime lastPlayedAt,
        LocalDateTime unlockedAt
) {}
