package com.exe.buddy_english_be.modules.progress.dto;

import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdventureProgressResponse(
        Long id,
        Long adventureId,
        String adventureName,
        ProgressStatus status,
        Integer score,
        Integer bestScore,
        Integer attemptCount,
        LocalDateTime lastPlayedAt,
        LocalDateTime completedAt
) {}
