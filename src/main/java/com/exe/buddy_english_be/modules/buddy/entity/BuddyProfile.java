package com.exe.buddy_english_be.modules.buddy.entity;

import com.exe.buddy_english_be.modules.buddy.enums.BuddyMood;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "buddy_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuddyProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false, unique = true)
    private ChildProfile child;

    @Column(name = "name", length = 50)
    @Builder.Default
    private String name = "Buddy";

    @Column(name = "level")
    @Builder.Default
    private Integer level = 1;

    @Column(name = "friendship_points")
    @Builder.Default
    private Integer friendshipPoints = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "mood", length = 20)
    @Builder.Default
    private BuddyMood mood = BuddyMood.HAPPY;

    @Column(name = "energy")
    @Builder.Default
    private Integer energy = 100;

    @Column(name = "last_interaction_at")
    private LocalDateTime lastInteractionAt;
}
