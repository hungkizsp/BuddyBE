package com.exe.buddy_english_be.modules.reward.dto;

import com.exe.buddy_english_be.modules.reward.enums.RewardRarity;
import com.exe.buddy_english_be.modules.reward.enums.RewardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateRewardRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "Type is required")
        RewardType type,
        String imageUrl,
        Integer price,
        RewardRarity rarity,
        Boolean isLimited,
        LocalDateTime availableFrom,
        LocalDateTime availableTo
) {}
