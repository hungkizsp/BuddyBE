package com.exe.buddy_english_be.modules.buddy.entity;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "child_memories",
        indexes = {
                @Index(name = "idx_mem_child", columnList = "child_id"),
                @Index(name = "idx_mem_key", columnList = "memory_key")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildMemory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @Column(name = "memory_type", length = 50)
    private String memoryType;

    @Column(name = "memory_key", length = 100)
    private String memoryKey;

    @Column(name = "memory_value", columnDefinition = "nvarchar(500)")
    private String memoryValue;

    @Column(name = "confidence")
    private Double confidence;

    // VOICE, MISSION, CHAT, SYSTEM
    @Column(name = "source", length = 20)
    private String source;

    @Column(name = "importance")
    @Builder.Default
    private Integer importance = 1;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
}
