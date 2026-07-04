package com.exe.buddy_english_be.modules.notification.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        Long childId,
        String title,
        String message,
        String type,
        Boolean isRead,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
