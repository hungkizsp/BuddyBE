package com.exe.buddy_english_be.modules.conversation.entity;

import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Entity Only — Service/Controller to be implemented in future
@Entity
@Table(name = "ai_analysis_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiAnalysisLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id")
    private SpeakingAttempt attempt;

    @Column(name = "request_json", columnDefinition = "nvarchar(max)")
    private String requestJson;

    @Column(name = "response_json", columnDefinition = "nvarchar(max)")
    private String responseJson;

    @Column(name = "latency_ms")
    private Long latencyMs;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
