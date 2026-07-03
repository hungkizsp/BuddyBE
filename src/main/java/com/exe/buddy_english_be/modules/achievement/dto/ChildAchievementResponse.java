package com.exe.buddy_english_be.modules.achievement.dto;

import java.time.LocalDateTime;

public record ChildAchievementResponse(
        Long id,
        Long childId,
        Long achievementId,
        String achievementName,
        String achievementIcon,
        Integer progressValue,
        LocalDateTime earnedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
