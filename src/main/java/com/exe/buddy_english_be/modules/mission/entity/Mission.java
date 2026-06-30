package com.exe.buddy_english_be.modules.mission.entity;

import com.exe.buddy_english_be.modules.mission.enums.MissionFrequency;
import com.exe.buddy_english_be.modules.mission.enums.TargetType;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "missions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mission extends BaseEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "nvarchar(500)")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_frequency", length = 20)
    @Builder.Default
    private MissionFrequency missionFrequency = MissionFrequency.DAILY;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", length = 30)
    private TargetType targetType;

    @Column(name = "target_value")
    @Builder.Default
    private Integer targetValue = 1;

    @Column(name = "reward_coin")
    @Builder.Default
    private Integer rewardCoin = 0;

    @Column(name = "reward_xp")
    @Builder.Default
    private Integer rewardXp = 0;
}
