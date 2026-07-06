package com.exe.buddy_english_be.modules.buddy.dto;

import com.exe.buddy_english_be.modules.buddy.enums.BuddyMood;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateBuddyProfileRequest(
        @NotNull(message = "Child ID is required")
        Long childId,
        String name,
        Integer level,
        Integer friendshipPoints,
        BuddyMood mood,
        Integer energy,
        LocalDateTime lastInteractionAt
) {}
