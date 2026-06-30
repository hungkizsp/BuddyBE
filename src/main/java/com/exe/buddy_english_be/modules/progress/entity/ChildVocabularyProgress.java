package com.exe.buddy_english_be.modules.progress.entity;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "child_vocabulary_progress",
        uniqueConstraints = @UniqueConstraint(name = "uc_child_vocab", columnNames = {"child_id", "vocabulary_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildVocabularyProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    // 1-5 mastery scale
    @Column(name = "mastery_level")
    @Builder.Default
    private Integer masteryLevel = 1;

    @Column(name = "correct_count")
    @Builder.Default
    private Integer correctCount = 0;

    @Column(name = "wrong_count")
    @Builder.Default
    private Integer wrongCount = 0;

    // Spaced repetition: confidence score for smart review
    @Column(name = "confidence_score")
    @Builder.Default
    private Double confidenceScore = 0.0;

    @Column(name = "first_learned_at")
    private LocalDateTime firstLearnedAt;

    // Next scheduled review (spaced repetition algorithm)
    @Column(name = "next_review_at")
    private LocalDateTime nextReviewAt;

    @Column(name = "last_practiced")
    private LocalDateTime lastPracticed;
}
