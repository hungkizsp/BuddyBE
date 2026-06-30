package com.exe.buddy_english_be.modules.conversation.entity;

import com.exe.buddy_english_be.modules.conversation.enums.MessageSender;
import com.exe.buddy_english_be.modules.conversation.enums.MessageType;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversation_messages",
        indexes = {
                @Index(name = "idx_msg_session", columnList = "session_id"),
                @Index(name = "idx_msg_created", columnList = "created_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationMessage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ConversationSession session;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender", length = 10, nullable = false)
    private MessageSender sender;

    @Column(name = "message", columnDefinition = "nvarchar(2000)")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 20)
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "intent", length = 100)
    private String intent;

    // JSON string of detected entities - nvarchar(max) for SQL Server
    @Column(name = "entities_json", columnDefinition = "nvarchar(max)")
    private String entitiesJson;

    @Column(name = "emotion", length = 50)
    private String emotion;

    @Column(name = "confidence")
    private Double confidence;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onMessageCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
