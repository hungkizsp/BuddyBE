package com.exe.buddy_english_be.modules.buddy.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UpdateChildMemoryRequest(
        String memoryType,
        String memoryKey,
        String memoryValue,
        Double confidence,
        String source,
        Integer importance,
        Boolean isActive,
        LocalDateTime lastUsedAt
) {}
