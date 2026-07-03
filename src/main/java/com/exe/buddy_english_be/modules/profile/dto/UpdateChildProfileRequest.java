package com.exe.buddy_english_be.modules.profile.dto;

import com.exe.buddy_english_be.modules.profile.enums.Gender;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateChildProfileRequest(
        @Size(max = 100, message = "Nickname must not exceed 100 characters")
        String nickname,

        @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
        String avatarUrl,

        LocalDate birthDate,

        Gender gender
) {}
