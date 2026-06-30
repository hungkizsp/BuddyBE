package com.exe.buddy_english_be.modules.reward.entity;

import com.exe.buddy_english_be.modules.reward.enums.RewardRarity;
import com.exe.buddy_english_be.modules.reward.enums.RewardType;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 30)
    private RewardType type;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "price")
    @Builder.Default
    private Integer price = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "rarity", length = 20)
    @Builder.Default
    private RewardRarity rarity = RewardRarity.COMMON;

    @Column(name = "is_limited")
    @Builder.Default
    private Boolean isLimited = false;

    @Column(name = "available_from")
    private LocalDateTime availableFrom;

    @Column(name = "available_to")
    private LocalDateTime availableTo;
}
