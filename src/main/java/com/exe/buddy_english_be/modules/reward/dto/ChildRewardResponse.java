package com.exe.buddy_english_be.modules.reward.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChildRewardResponse(
        Long id,
        Long childId,
        Long rewardId,
        Boolean equipped,
        LocalDateTime obtainedAt
) {}
