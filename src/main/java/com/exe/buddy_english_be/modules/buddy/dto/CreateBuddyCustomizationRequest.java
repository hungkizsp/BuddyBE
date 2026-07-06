package com.exe.buddy_english_be.modules.buddy.dto;

import com.exe.buddy_english_be.modules.buddy.enums.CustomizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateBuddyCustomizationRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "Type is required")
        CustomizationType type,
        String imageUrl,
        Integer price
) {}
