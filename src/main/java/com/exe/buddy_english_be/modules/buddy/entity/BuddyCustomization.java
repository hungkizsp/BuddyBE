package com.exe.buddy_english_be.modules.buddy.entity;

import com.exe.buddy_english_be.modules.buddy.enums.CustomizationType;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

// Entity Only — Service/Controller to be implemented in future
@Entity
@Table(name = "buddy_customizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuddyCustomization extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private CustomizationType type;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "price")
    @Builder.Default
    private Integer price = 0;
}
