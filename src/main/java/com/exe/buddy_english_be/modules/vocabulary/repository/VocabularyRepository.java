package com.exe.buddy_english_be.modules.vocabulary.repository;

import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {

    List<Vocabulary> findByCategoryId(Long categoryId);
}
