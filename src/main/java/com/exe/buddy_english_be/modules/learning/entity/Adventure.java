package com.exe.buddy_english_be.modules.learning.entity;

import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "adventures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adventure extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_id", nullable = false)
    private World world;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "nvarchar(500)")
    private String description;

    @Column(name = "thumbnail", length = 500)
    private String thumbnail;

    @Column(name = "difficulty", length = 20)
    private String difficulty;

    @Column(name = "order_index")
    @Builder.Default
    private Integer orderIndex = 0;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
}
