package com.exe.buddy_english_be.modules.reward.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateChildRewardRequest(
        @NotNull(message = "Child ID is required")
        Long childId,
        @NotNull(message = "Reward ID is required")
        Long rewardId,
        Boolean equipped,
        LocalDateTime obtainedAt
) {}
