package com.exe.buddy_english_be.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record GoogleLoginRequest(
        @NotBlank(message = "ID token or email is required")
        String idToken,
        String email,
        String name
) {}
