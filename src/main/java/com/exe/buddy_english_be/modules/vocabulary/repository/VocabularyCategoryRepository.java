package com.exe.buddy_english_be.modules.vocabulary.repository;

import com.exe.buddy_english_be.modules.vocabulary.entity.VocabularyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VocabularyCategoryRepository extends JpaRepository<VocabularyCategory, Long> {

    Optional<VocabularyCategory> findByName(String name);
}
