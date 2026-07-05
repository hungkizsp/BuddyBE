package com.exe.buddy_english_be.modules.user.dto;

import com.exe.buddy_english_be.modules.user.enums.UserStatus;
import lombok.Builder;

@Builder
public record UpdateUserStatusRequest(
        UserStatus status
) {}
