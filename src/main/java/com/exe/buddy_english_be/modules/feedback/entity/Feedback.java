package com.exe.buddy_english_be.modules.feedback.entity;

import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// Entity Only — Service/Controller to be implemented in future
@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment", columnDefinition = "nvarchar(1000)")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
