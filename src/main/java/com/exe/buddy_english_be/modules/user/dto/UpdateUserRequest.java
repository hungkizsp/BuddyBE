package com.exe.buddy_english_be.modules.user.dto;

import com.exe.buddy_english_be.modules.user.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserRequest(

        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        UserStatus status
) {}
