package com.exe.buddy_english_be.modules.studymode.controller;

import com.exe.buddy_english_be.modules.studymode.dto.HighlightRequest;
import com.exe.buddy_english_be.modules.studymode.dto.HighlightResponse;
import com.exe.buddy_english_be.modules.studymode.service.HighlightService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/highlights")
@RequiredArgsConstructor
public class HighlightController {

    private final HighlightService highlightService;

    /**
     * GET /api/highlights?childId={childId}&vocabularyId={vocabularyId}
     * Returns the stored highlight data or null if none exists.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<HighlightResponse>> getHighlight(
            @RequestParam Long childId,
            @RequestParam Long vocabularyId) {
        HighlightResponse response = highlightService.getHighlight(childId, vocabularyId);
        return ResponseEntity.ok(ApiResponse.success("Highlight fetched successfully", response));
    }

    /**
     * PUT /api/highlights
     * Creates or updates (upsert) highlight data for a child+vocabulary pair.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<HighlightResponse>> saveHighlight(
            @Valid @RequestBody HighlightRequest request) {
        HighlightResponse response = highlightService.saveHighlight(request);
        return ResponseEntity.ok(ApiResponse.success("Highlight saved successfully", response));
    }
}
