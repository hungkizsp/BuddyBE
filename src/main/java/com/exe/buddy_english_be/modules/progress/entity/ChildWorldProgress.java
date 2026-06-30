package com.exe.buddy_english_be.modules.progress.entity;

import com.exe.buddy_english_be.modules.learning.entity.World;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "child_world_progress",
        uniqueConstraints = @UniqueConstraint(name = "uc_child_world", columnNames = {"child_id", "world_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildWorldProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_id", nullable = false)
    private World world;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @Builder.Default
    private ProgressStatus status = ProgressStatus.LOCKED;

    @Column(name = "completion_percentage")
    @Builder.Default
    private Integer completionPercentage = 0;

    @Column(name = "last_played_at")
    private LocalDateTime lastPlayedAt;

    @Column(name = "unlocked_at")
    private LocalDateTime unlockedAt;
}
