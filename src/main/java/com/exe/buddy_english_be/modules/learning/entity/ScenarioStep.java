package com.exe.buddy_english_be.modules.learning.entity;

import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scenario_steps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScenarioStep extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "buddy_message", columnDefinition = "nvarchar(1000)")
    private String buddyMessage;

    @Column(name = "expected_intent", length = 100)
    private String expectedIntent;

    @Column(name = "expected_entity", length = 100)
    private String expectedEntity;

    @Column(name = "success_response", columnDefinition = "nvarchar(500)")
    private String successResponse;

    @Column(name = "fail_response", columnDefinition = "nvarchar(500)")
    private String failResponse;

    // Self-join for basic branching. Future: scenario_step_transitions table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "next_step_id")
    private ScenarioStep nextStep;
}
