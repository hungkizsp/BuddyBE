package com.exe.buddy_english_be.modules.profile.dto;

import lombok.Builder;

@Builder
public record ParentProfileResponse(
        Long id,
        Long userId,
        String fullName,
        String phone,
        String occupation,
        String avatarUrl
) {}
