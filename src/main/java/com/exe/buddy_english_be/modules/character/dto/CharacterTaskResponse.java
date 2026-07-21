package com.exe.buddy_english_be.modules.character.dto;

import com.exe.buddy_english_be.modules.character.enums.CharacterStatus;

import java.time.LocalDateTime;

public record CharacterTaskResponse(
        Long id,
        String taskId,
        String characterName,
        String prompt,
        String artStyle,
        CharacterStatus status,
        Integer progress,
        String localModelPath,
        String thumbnailUrl,
        LocalDateTime createdAt
) {}
