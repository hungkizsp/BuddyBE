package com.exe.buddy_english_be.modules.profile.entity;

import com.exe.buddy_english_be.modules.learning.entity.Scenario;
import com.exe.buddy_english_be.modules.learning.entity.World;
import com.exe.buddy_english_be.modules.profile.enums.Gender;
import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "child_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Column(name = "level")
    @Builder.Default
    private Integer level = 1;

    @Column(name = "xp")
    @Builder.Default
    private Integer xp = 0;

    @Column(name = "coins")
    @Builder.Default
    private Integer coins = 0;

    @Column(name = "streak_days")
    @Builder.Default
    private Integer streakDays = 0;

    @Column(name = "last_login_date")
    private LocalDate lastLoginDate;

    // Continue Learning shortcuts (nullable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_world_id")
    private World lastWorld;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_scenario_id")
    private Scenario lastScenario;

    @Column(name = "last_session_at")
    private LocalDateTime lastSessionAt;
}
