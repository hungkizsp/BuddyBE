package com.exe.buddy_english_be.modules.reward.dto;

import com.exe.buddy_english_be.modules.reward.enums.RewardRarity;
import com.exe.buddy_english_be.modules.reward.enums.RewardType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RewardResponse(
        Long id,
        String name,
        RewardType type,
        String imageUrl,
        Integer price,
        RewardRarity rarity,
        Boolean isLimited,
        LocalDateTime availableFrom,
        LocalDateTime availableTo
) {}
