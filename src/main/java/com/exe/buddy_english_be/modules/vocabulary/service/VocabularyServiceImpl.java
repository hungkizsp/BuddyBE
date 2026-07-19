package com.exe.buddy_english_be.modules.vocabulary.service;

import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyRequest;
import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyResponse;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.entity.VocabularyCategory;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyCategoryRepository;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VocabularyServiceImpl implements VocabularyService {
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyCategoryRepository vocabularyCategoryRepository;

    public VocabularyServiceImpl(
            VocabularyRepository vocabularyRepository,
            VocabularyCategoryRepository vocabularyCategoryRepository) {
        this.vocabularyRepository = vocabularyRepository;
        this.vocabularyCategoryRepository = vocabularyCategoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VocabularyResponse> getAllVocabularies() {
        return vocabularyRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VocabularyResponse> getVocabulariesByCategoryId(Long categoryId) {
        return vocabularyRepository.findByCategoryId(categoryId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VocabularyResponse getVocabularyById(Long id) {
        return toResponse(findVocabulary(id));
    }

    @Override
    @Transactional
    public VocabularyResponse createVocabulary(VocabularyRequest request) {
        Vocabulary vocabulary = Vocabulary.builder()
                .category(findCategory(request.categoryId()))
                .word(request.word().trim())
                .phonetic(normalize(request.phonetic()))
                .meaning(normalize(request.meaning()))
                .exampleSentence(normalize(request.exampleSentence()))
                .imageUrl(normalize(request.imageUrl()))
                .audioUrl(normalize(request.audioUrl()))
                .difficulty(normalize(request.difficulty()))
                .build();

        return toResponse(vocabularyRepository.save(vocabulary));
    }

    @Override
    @Transactional
    public VocabularyResponse updateVocabulary(Long id, VocabularyRequest request) {
        Vocabulary vocabulary = findVocabulary(id);
        vocabulary.setCategory(findCategory(request.categoryId()));
        vocabulary.setWord(request.word().trim());
        vocabulary.setPhonetic(normalize(request.phonetic()));
        vocabulary.setMeaning(normalize(request.meaning()));
        vocabulary.setExampleSentence(normalize(request.exampleSentence()));
        vocabulary.setImageUrl(normalize(request.imageUrl()));
        vocabulary.setAudioUrl(normalize(request.audioUrl()));
        vocabulary.setDifficulty(normalize(request.difficulty()));

        return toResponse(vocabularyRepository.save(vocabulary));
    }

    @Override
    @Transactional
    public void deleteVocabulary(Long id) {
        Vocabulary vocabulary = findVocabulary(id);
        vocabularyRepository.delete(vocabulary);
    }

    private Vocabulary findVocabulary(Long id) {
        return vocabularyRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.VOCABULARY_NOT_FOUND));
    }

    private VocabularyCategory findCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        return vocabularyCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VOCABULARY_CATEGORY_NOT_FOUND));
    }

    private VocabularyResponse toResponse(Vocabulary vocabulary) {
        VocabularyCategory category = vocabulary.getCategory();
        return new VocabularyResponse(
                vocabulary.getId(),
                category != null ? category.getId() : null,
                category != null ? category.getName() : null,
                vocabulary.getWord(),
                vocabulary.getPhonetic(),
                vocabulary.getMeaning(),
                vocabulary.getExampleSentence(),
                vocabulary.getImageUrl(),
                vocabulary.getAudioUrl(),
                vocabulary.getDifficulty(),
                vocabulary.getCreatedAt(),
                vocabulary.getUpdatedAt()
        );
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
