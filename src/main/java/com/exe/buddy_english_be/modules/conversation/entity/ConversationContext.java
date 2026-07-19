package com.exe.buddy_english_be.modules.conversation.entity;

import java.time.LocalDateTime;

import com.exe.buddy_english_be.modules.conversation.enums.ContextType;
import com.exe.buddy_english_be.shared.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conversation_context",
        uniqueConstraints = @UniqueConstraint(
                name = "uc_session_key_type",
                columnNames = {"session_id", "context_key", "context_type"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationContext extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;

    @Column(name = "context_key", nullable = false, length = 100)
    private String contextKey;

    @Column(name = "context_value", columnDefinition = "nvarchar(max)")
    private String contextValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "context_type", length = 20, nullable = false)
    private ContextType contextType;

    @Column(name = "importance")
    @Builder.Default
    private Integer importance = 1;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    // For SHORT_TERM / MISSION context expiry
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
