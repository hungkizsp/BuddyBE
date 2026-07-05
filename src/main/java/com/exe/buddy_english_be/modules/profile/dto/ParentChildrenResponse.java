package com.exe.buddy_english_be.modules.profile.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ParentChildrenResponse(
        Long parentId,
        List<ChildSummary> children
) {
    @Builder
    public record ChildSummary(
            Long id,
            Long childUserId,
            String nickname,
            String avatarUrl,
            Integer level,
            Integer xp,
            String relationship
    ) {}
}
