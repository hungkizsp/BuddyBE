package com.exe.buddy_english_be.modules.learning.entity;

import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

// Entity Only — Service/Controller to be implemented in future
@Entity
@Table(name = "scenario_vocabularies",
        uniqueConstraints = @UniqueConstraint(name = "uc_scenario_vocab", columnNames = {"scenario_id", "vocabulary_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScenarioVocabulary extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;
}
