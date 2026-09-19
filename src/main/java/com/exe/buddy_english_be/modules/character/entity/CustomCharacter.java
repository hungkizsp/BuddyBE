package com.exe.buddy_english_be.modules.character.entity;

import com.exe.buddy_english_be.modules.character.enums.CharacterStatus;
import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "custom_characters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomCharacter extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "task_id", length = 255)
    private String taskId;

    @Column(name = "character_name", length = 100)
    private String characterName;

    @Column(name = "prompt", columnDefinition = "NVARCHAR(MAX)")
    private String prompt;

    @Column(name = "art_style", length = 50)
    private String artStyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private CharacterStatus status = CharacterStatus.PENDING;

    @Column(name = "progress")
    @Builder.Default
    private Integer progress = 0;

    // Relative URL path served by Vite static folder, e.g. "/custom_models/abc.glb"
    @Column(name = "local_model_path", length = 500)
    private String localModelPath;

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;
}
