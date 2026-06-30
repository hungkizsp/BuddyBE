package com.exe.buddy_english_be.modules.auth.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record LoginResponse(
        Long id,
        String email,
        String nickname,
        List<String> roles,
        Integer level,
        Integer xp,
        Integer coins
) {
}
