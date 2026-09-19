package com.exe.buddy_english_be.modules.studymode.service;

import com.exe.buddy_english_be.modules.studymode.dto.HighlightRequest;
import com.exe.buddy_english_be.modules.studymode.dto.HighlightResponse;

public interface HighlightService {
    /** Get highlight for a specific child+vocabulary pair. Returns null if none exists. */
    HighlightResponse getHighlight(Long childId, Long vocabularyId);

    /** Create or update (upsert) highlight data. */
    HighlightResponse saveHighlight(HighlightRequest request);
}
