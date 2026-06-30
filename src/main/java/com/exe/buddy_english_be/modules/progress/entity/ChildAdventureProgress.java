package com.exe.buddy_english_be.modules.progress.entity;

import com.exe.buddy_english_be.modules.learning.entity.Adventure;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "child_adventure_progress",
        uniqueConstraints = @UniqueConstraint(name = "uc_child_adventure", columnNames = {"child_id", "adventure_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildAdventureProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adventure_id", nullable = false)
    private Adventure adventure;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @Builder.Default
    private ProgressStatus status = ProgressStatus.LOCKED;

    @Column(name = "score")
    @Builder.Default
    private Integer score = 0;

    @Column(name = "best_score")
    @Builder.Default
    private Integer bestScore = 0;

    @Column(name = "attempt_count")
    @Builder.Default
    private Integer attemptCount = 0;

    @Column(name = "last_played_at")
    private LocalDateTime lastPlayedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
