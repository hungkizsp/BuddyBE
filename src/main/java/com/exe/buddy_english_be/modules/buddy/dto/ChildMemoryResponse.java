package com.exe.buddy_english_be.modules.buddy.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChildMemoryResponse(
        Long id,
        Long childId,
        String memoryType,
        String memoryKey,
        String memoryValue,
        Double confidence,
        String source,
        Integer importance,
        Boolean isActive,
        LocalDateTime lastUsedAt
) {}
