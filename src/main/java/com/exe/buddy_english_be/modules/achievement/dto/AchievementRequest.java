package com.exe.buddy_english_be.modules.achievement.dto;

import com.exe.buddy_english_be.modules.reward.enums.RewardType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AchievementRequest(
        @NotBlank(message = "name is required")
        @Size(max = 100, message = "name must be at most 100 characters")
        String name,

        @Size(max = 500, message = "description must be at most 500 characters")
        String description,

        @Size(max = 500, message = "icon must be at most 500 characters")
        String icon,

        @Size(max = 50, message = "conditionType must be at most 50 characters")
        String conditionType,

        @Min(value = 0, message = "conditionValue must be at least 0")
        Integer conditionValue,

        String conditionJson,

        @Min(value = 0, message = "rewardCoin must be at least 0")
        Integer rewardCoin,

        @Min(value = 0, message = "rewardXp must be at least 0")
        Integer rewardXp,

        RewardType rewardType,

        @Size(max = 100, message = "rewardValue must be at most 100 characters")
        String rewardValue
) {
}
