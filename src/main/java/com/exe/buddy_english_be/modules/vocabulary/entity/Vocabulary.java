package com.exe.buddy_english_be.modules.vocabulary.entity;

import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vocabularies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vocabulary extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private VocabularyCategory category;

    @Column(name = "word", nullable = false, length = 100)
    private String word;

    @Column(name = "phonetic", length = 100)
    private String phonetic;

    @Column(name = "meaning", columnDefinition = "nvarchar(500)")
    private String meaning;

    @Column(name = "example_sentence", columnDefinition = "nvarchar(500)")
    private String exampleSentence;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "difficulty", length = 20)
    private String difficulty;
}
