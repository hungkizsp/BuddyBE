package com.exe.buddy_english_be.modules.studymode.service;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.studymode.dto.HighlightRequest;
import com.exe.buddy_english_be.modules.studymode.dto.HighlightResponse;
import com.exe.buddy_english_be.modules.studymode.entity.VocabHighlight;
import com.exe.buddy_english_be.modules.studymode.repository.VocabHighlightRepository;
import com.exe.buddy_english_be.modules.vocabulary.entity.Vocabulary;
import com.exe.buddy_english_be.modules.vocabulary.repository.VocabularyRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HighlightServiceImpl implements HighlightService {

    private final VocabHighlightRepository highlightRepository;
    private final ChildProfileRepository childProfileRepository;
    private final VocabularyRepository vocabularyRepository;

    @Override
    @Transactional(readOnly = true)
    public HighlightResponse getHighlight(Long childId, Long vocabularyId) {
        return highlightRepository.findByChildIdAndVocabularyId(childId, vocabularyId)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional
    public HighlightResponse saveHighlight(HighlightRequest request) {
        ChildProfile child = childProfileRepository.findById(request.childId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
        Vocabulary vocab = vocabularyRepository.findById(request.vocabularyId())
                .orElseThrow(() -> new BusinessException(ErrorCode.VOCABULARY_NOT_FOUND));

        // Upsert: find existing or create new
        VocabHighlight highlight = highlightRepository
                .findByChildIdAndVocabularyId(request.childId(), request.vocabularyId())
                .orElseGet(() -> VocabHighlight.builder()
                        .child(child)
                        .vocabulary(vocab)
                        .build());

        highlight.setHighlightData(request.highlightData());
        highlight.setUserNote(request.userNote());

        return toResponse(highlightRepository.save(highlight));
    }

    private HighlightResponse toResponse(VocabHighlight h) {
        return new HighlightResponse(
                h.getId(),
                h.getChild().getId(),
                h.getVocabulary().getId(),
                h.getHighlightData(),
                h.getUserNote(),
                h.getUpdatedAt()
        );
    }
}
