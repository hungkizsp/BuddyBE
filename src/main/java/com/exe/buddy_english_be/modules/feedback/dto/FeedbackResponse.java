package com.exe.buddy_english_be.modules.feedback.dto;

import java.time.LocalDateTime;

public record FeedbackResponse(
        Long id,
        Long userId,
        String userEmail,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {
}
