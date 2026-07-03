package com.exe.buddy_english_be.modules.user.dto;

import com.exe.buddy_english_be.modules.user.enums.UserStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UserResponse(
        Long id,
        String email,
        UserStatus status,
        Set<String> roles,
        LocalDateTime createdAt
) {}
