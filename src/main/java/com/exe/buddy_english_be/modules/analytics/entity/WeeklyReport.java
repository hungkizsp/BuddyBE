package com.exe.buddy_english_be.modules.analytics.entity;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Entity Only — generated from queries in the future
@Entity
@Table(name = "weekly_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @Column(name = "week_start")
    private LocalDate weekStart;

    @Column(name = "week_end")
    private LocalDate weekEnd;

    @Column(name = "learning_minutes")
    private Integer learningMinutes;

    @Column(name = "words_learned")
    private Integer wordsLearned;

    @Column(name = "speaking_attempts")
    private Integer speakingAttempts;

    @Column(name = "completed_scenarios")
    private Integer completedScenarios;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}
