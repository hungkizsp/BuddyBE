package com.exe.buddy_english_be.modules.vocabulary.service;

import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyRequest;
import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyResponse;

import java.util.List;

public interface VocabularyService {

    List<VocabularyResponse> getAllVocabularies();

    List<VocabularyResponse> getVocabulariesByCategoryId(Long categoryId);

    VocabularyResponse getVocabularyById(Long id);

    VocabularyResponse createVocabulary(VocabularyRequest request);

    VocabularyResponse updateVocabulary(Long id, VocabularyRequest request);

    void deleteVocabulary(Long id);
}
