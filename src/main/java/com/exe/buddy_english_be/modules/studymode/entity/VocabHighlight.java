package com.exe.buddy_english_be.modules.studymode.entity;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Stores per-child highlight annotations on vocabulary fields.
 * highlightData is stored as a JSON string:
 *   [{"field":"meaning","start":0,"end":5,"color":"#FFD700"}, ...]
 */
@Entity
@Table(
    name = "vocab_highlights",
    uniqueConstraints = @UniqueConstraint(
        name = "uc_child_vocab_highlight",
        columnNames = {"child_id", "vocabulary_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabHighlight extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    private ChildProfile child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    /**
     * JSON array: [{field:"meaning"|"exampleSentence", start:int, end:int, color:string}]
     */
    @Column(name = "highlight_data", columnDefinition = "nvarchar(MAX)")
    private String highlightData;

    @Column(name = "user_note", columnDefinition = "nvarchar(500)")
    private String userNote;
}
