package com.exe.buddy_english_be.modules.learning.repository;

import com.exe.buddy_english_be.modules.learning.entity.ScenarioVocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScenarioVocabularyRepository extends JpaRepository<ScenarioVocabulary, Long> {

    List<ScenarioVocabulary> findByScenarioId(Long scenarioId);

    Optional<ScenarioVocabulary> findByScenarioIdAndVocabularyId(Long scenarioId, Long vocabularyId);
}
