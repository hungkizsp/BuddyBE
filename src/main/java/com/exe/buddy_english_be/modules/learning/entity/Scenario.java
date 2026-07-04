package com.exe.buddy_english_be.modules.learning.entity;

import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scenarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scenario extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_id", nullable = false)
    private World world;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "nvarchar(500)")
    private String description;

    // dialogue, quiz, roleplay, etc.
    @Column(name = "scenario_type", length = 50)
    private String scenarioType;

    @Column(name = "expected_intent", length = 100)
    private String expectedIntent;

    @Column(name = "difficulty", length = 20)
    private String difficulty;

    @Column(name = "order_index")
    @Builder.Default
    private Integer orderIndex = 0;
}
