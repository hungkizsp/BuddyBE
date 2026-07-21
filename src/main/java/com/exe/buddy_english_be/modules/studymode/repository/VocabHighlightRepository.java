package com.exe.buddy_english_be.modules.studymode.repository;

import com.exe.buddy_english_be.modules.studymode.entity.VocabHighlight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VocabHighlightRepository extends JpaRepository<VocabHighlight, Long> {

    Optional<VocabHighlight> findByChildIdAndVocabularyId(Long childId, Long vocabularyId);
}
