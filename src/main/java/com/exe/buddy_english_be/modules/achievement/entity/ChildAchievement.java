package com.exe.buddy_english_be.modules.achievement.entity;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "child_achievements",
        uniqueConstraints = @UniqueConstraint(name = "uc_child_achievement", columnNames = {"child_id", "achievement_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildAchievement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "achievement_id", nullable = false)
    private Achievement achievement;

    // Shows progress bar: e.g. 65/100 words spoken
    @Column(name = "progress_value")
    @Builder.Default
    private Integer progressValue = 0;

    @Column(name = "earned_at")
    private LocalDateTime earnedAt;
}
