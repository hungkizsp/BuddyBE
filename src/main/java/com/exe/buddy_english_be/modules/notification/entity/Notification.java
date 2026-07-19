package com.exe.buddy_english_be.modules.notification.entity;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private ChildProfile child;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "message", columnDefinition = "nvarchar(500)")
    private String message;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "is_read")
    @Builder.Default
    private Boolean isRead = false;
}
