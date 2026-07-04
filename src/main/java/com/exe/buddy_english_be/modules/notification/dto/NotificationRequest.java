package com.exe.buddy_english_be.modules.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotificationRequest(

        @NotNull(message = "childId is required")
        Long childId,

        @NotBlank(message = "title is required")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @NotBlank(message = "message is required")
        @Size(max = 500, message = "message must be at most 500 characters")
        String message,

        @Size(max = 50, message = "type must be at most 50 characters")
        String type,

        Boolean isRead
) {
}
