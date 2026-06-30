package com.exe.buddy_english_be.modules.progress.repository;

import com.exe.buddy_english_be.modules.progress.entity.ChildVocabularyProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChildVocabularyProgressRepository extends JpaRepository<ChildVocabularyProgress, Long> {

    List<ChildVocabularyProgress> findByChildIdOrderByLastPracticedDesc(Long childId);

    Optional<ChildVocabularyProgress> findByChildIdAndVocabularyId(Long childId, Long vocabularyId);

    // For spaced repetition: words due for review
    List<ChildVocabularyProgress> findByChildIdAndNextReviewAtBefore(Long childId, LocalDateTime now);
}
