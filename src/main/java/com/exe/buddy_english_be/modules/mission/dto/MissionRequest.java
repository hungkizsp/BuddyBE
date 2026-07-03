package com.exe.buddy_english_be.modules.mission.dto;

import com.exe.buddy_english_be.modules.mission.enums.MissionFrequency;
import com.exe.buddy_english_be.modules.mission.enums.TargetType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MissionRequest(
        @NotBlank(message = "title is required")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @Size(max = 500, message = "description must be at most 500 characters")
        String description,

        @NotNull(message = "missionFrequency is required")
        MissionFrequency missionFrequency,

        @NotNull(message = "targetType is required")
        TargetType targetType,

        @NotNull(message = "targetValue is required")
        @Min(value = 1, message = "targetValue must be at least 1")
        Integer targetValue,

        @NotNull(message = "rewardCoin is required")
        @Min(value = 0, message = "rewardCoin must be at least 0")
        Integer rewardCoin,

        @NotNull(message = "rewardXp is required")
        @Min(value = 0, message = "rewardXp must be at least 0")
        Integer rewardXp
) {
}
