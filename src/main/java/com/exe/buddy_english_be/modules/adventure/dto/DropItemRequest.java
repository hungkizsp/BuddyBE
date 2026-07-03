package com.exe.buddy_english_be.modules.adventure.dto;

import jakarta.validation.constraints.NotBlank;

public record DropItemRequest(
        @NotBlank(message = "itemId is required")
        String itemId
) {
}
