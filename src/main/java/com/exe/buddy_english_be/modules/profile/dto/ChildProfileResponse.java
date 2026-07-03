package com.exe.buddy_english_be.modules.profile.dto;

import com.exe.buddy_english_be.modules.profile.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ChildProfileResponse(
        Long id,
        Long userId,
        String nickname,
        String avatarUrl,
        LocalDate birthDate,
        Gender gender,
        Integer level,
        Integer xp,
        Integer coins,
        Integer streakDays,
        LocalDate lastLoginDate,
        LocalDateTime lastSessionAt
) {}
