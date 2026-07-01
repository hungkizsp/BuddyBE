package com.exe.buddy_english_be.feature.adventure.entity;

import com.exe.buddy_english_be.feature.adventure.enums.AdventureLevelStatus;
import com.exe.buddy_english_be.feature.adventure.enums.GameMode;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "adventure_levels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdventureLevel extends BaseEntity {

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "estimated_time_minutes")
    private Integer estimatedTimeMinutes;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    @Builder.Default
    private AdventureLevelStatus status = AdventureLevelStatus.ACTIVE;

    @Column(name = "unlock_order")
    private Integer unlockOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "game_mode", length = 20)
    private GameMode gameMode;

    @Column(name = "min_points_required")
    @Builder.Default
    private Long minPointsRequired = 0L;

    // MappedBy refers to the 'level' field in ChildAdventureEntity
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildAdventure> childAdventures;

    // MappedBy refers to the 'level' field in GameSessionEntity
    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameSession> gameSessions;
}