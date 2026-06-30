package com.exe.buddy_english_be.modules.achievement.entity;

import com.exe.buddy_english_be.modules.reward.enums.RewardType;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achievement extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(500)")
    private String description;

    @Column(name = "icon", length = 500)
    private String icon;

    @Column(name = "condition_type", length = 50)
    private String conditionType;

    @Column(name = "condition_value")
    private Integer conditionValue;

    // Optional: for complex conditions in the future
    @Column(name = "condition_json", columnDefinition = "nvarchar(max)")
    private String conditionJson;

    @Column(name = "reward_coin")
    @Builder.Default
    private Integer rewardCoin = 0;

    @Column(name = "reward_xp")
    @Builder.Default
    private Integer rewardXp = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", length = 30)
    private RewardType rewardType;

    // COIN → "100", TITLE → "ANIMAL_MASTER", ITEM → "12"
    @Column(name = "reward_value", length = 100)
    private String rewardValue;
}
