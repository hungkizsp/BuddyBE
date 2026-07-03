package com.exe.buddy_english_be.modules.adventure.dto;

import jakarta.validation.constraints.NotBlank;

public record CombineItemsRequest(
        @NotBlank(message = "draggedId is required")
        String draggedId,

        @NotBlank(message = "targetId is required")
        String targetId
) {
}
