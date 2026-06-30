package com.exe.buddy_english_be.modules.buddy.entity;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Entity Only — Service/Controller to be implemented in future
@Entity
@Table(name = "child_customizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChildCustomization extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customization_id", nullable = false)
    private BuddyCustomization customization;

    @Column(name = "equipped")
    @Builder.Default
    private Boolean equipped = false;

    @Column(name = "obtained_at")
    private LocalDateTime obtainedAt;
}
