package com.exe.buddy_english_be.modules.learning.repository;

import com.exe.buddy_english_be.modules.learning.entity.ScenarioVocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScenarioVocabularyRepository extends JpaRepository<ScenarioVocabulary, Long> {
}
