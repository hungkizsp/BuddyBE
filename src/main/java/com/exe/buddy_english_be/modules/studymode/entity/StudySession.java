package com.exe.buddy_english_be.modules.studymode.entity;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.studymode.enums.StudyMode;
import com.exe.buddy_english_be.modules.vocabulary.entity.VocabularyCategory;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Tracks a single study session: which child, which category, which mode, and the outcome.
 */
@Entity
@Table(name = "study_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudySession extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private VocabularyCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false, length = 20)
    private StudyMode mode;

    @Column(name = "total_words")
    @Builder.Default
    private Integer totalWords = 0;

    @Column(name = "correct_count")
    @Builder.Default
    private Integer correctCount = 0;

    @Column(name = "wrong_count")
    @Builder.Default
    private Integer wrongCount = 0;

    /** Score 0–100 */
    @Column(name = "score")
    @Builder.Default
    private Integer score = 0;

    /** How long the session took in seconds */
    @Column(name = "duration_seconds")
    @Builder.Default
    private Integer durationSeconds = 0;

    /** Set when the session is fully completed */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /** True once the session is finished (answer all cards/questions) */
    @Column(name = "is_completed")
    @Builder.Default
    private Boolean isCompleted = false;
}
