package com.exe.buddy_english_be.modules.buddy.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateChildCustomizationRequest(
        Boolean equipped,
        LocalDateTime obtainedAt
) {}
