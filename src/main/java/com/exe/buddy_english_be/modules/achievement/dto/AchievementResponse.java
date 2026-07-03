package com.exe.buddy_english_be.modules.achievement.dto;

import com.exe.buddy_english_be.modules.reward.enums.RewardType;

import java.time.LocalDateTime;

public record AchievementResponse(
        Long id,
        String name,
        String description,
        String icon,
        String conditionType,
        Integer conditionValue,
        String conditionJson,
        Integer rewardCoin,
        Integer rewardXp,
        RewardType rewardType,
        String rewardValue,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
