package com.exe.buddy_english_be.modules.conversation.entity;

import com.exe.buddy_english_be.modules.conversation.enums.ProcessingStatus;
import com.exe.buddy_english_be.modules.learning.entity.Scenario;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "speaking_attempts",
        indexes = {
                @Index(name = "idx_speak_child", columnList = "child_id"),
                @Index(name = "idx_speak_scenario", columnList = "scenario_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpeakingAttempt extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private ConversationSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    // Raw speech-to-text output
    @Column(name = "recognized_text", columnDefinition = "nvarchar(1000)")
    private String recognizedText;

    // AI-corrected version
    @Column(name = "corrected_text", columnDefinition = "nvarchar(1000)")
    private String correctedText;

    @Column(name = "intent", length = 100)
    private String intent;

    @Column(name = "emotion", length = 50)
    private String emotion;

    @Column(name = "confidence")
    private Double confidence;

    // Future: Gemini / Azure Speech / Google Speech pronunciation score
    @Column(name = "pronunciation_score")
    private Double pronunciationScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "processing_status", length = 20)
    @Builder.Default
    private ProcessingStatus processingStatus = ProcessingStatus.PENDING;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onAttemptCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
