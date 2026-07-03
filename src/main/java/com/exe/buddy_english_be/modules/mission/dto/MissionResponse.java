package com.exe.buddy_english_be.modules.mission.dto;

import com.exe.buddy_english_be.modules.mission.enums.MissionFrequency;
import com.exe.buddy_english_be.modules.mission.enums.TargetType;

import java.time.LocalDateTime;

public record MissionResponse(
        Long id,
        String title,
        String description,
        MissionFrequency missionFrequency,
        TargetType targetType,
        Integer targetValue,
        Integer rewardCoin,
        Integer rewardXp,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
