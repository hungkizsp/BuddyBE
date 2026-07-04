package com.exe.buddy_english_be.modules.profile.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateParentProfileRequest(

        @NotNull(message = "userId is required")
        Long userId,

        @Size(max = 100, message = "Full name must not exceed 100 characters")
        String fullName,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        String phone,

        @Size(max = 100, message = "Occupation must not exceed 100 characters")
        String occupation,

        @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
        String avatarUrl
) {}
