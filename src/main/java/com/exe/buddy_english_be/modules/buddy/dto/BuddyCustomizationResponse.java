package com.exe.buddy_english_be.modules.buddy.dto;

import com.exe.buddy_english_be.modules.buddy.enums.CustomizationType;
import lombok.Builder;

@Builder
public record BuddyCustomizationResponse(
        Long id,
        String name,
        CustomizationType type,
        String imageUrl,
        Integer price
) {}
