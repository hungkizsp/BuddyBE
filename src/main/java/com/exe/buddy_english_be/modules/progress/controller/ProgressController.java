package com.exe.buddy_english_be.modules.progress.controller;

import com.exe.buddy_english_be.modules.progress.dto.AdventureProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.ChildProgressSummaryResponse;
import com.exe.buddy_english_be.modules.progress.dto.VocabularyProgressResponse;
import com.exe.buddy_english_be.modules.progress.dto.WorldProgressResponse;
import com.exe.buddy_english_be.modules.progress.service.ProgressService;
import com.exe.buddy_english_be.shared.response.ApiResponse;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@AllArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    /**
     * Returns the full progress summary for the authenticated child:
     * worlds, adventures, vocabulary, XP, coins, streaks.
     */
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ChildProgressSummaryResponse>> getProgressSummary(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ChildProgressSummaryResponse response = progressService.getProgressSummary(userId);
        return ResponseEntity.ok(ApiResponse.success("Progress summary fetched successfully", response));
    }

    /** Returns world-level progress for the authenticated child. */
    @GetMapping("/worlds")
    public ResponseEntity<ApiResponse<List<WorldProgressResponse>>> getWorldProgress(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<WorldProgressResponse> response = progressService.getWorldProgress(userId);
        return ResponseEntity.ok(ApiResponse.success("World progress fetched successfully", response));
    }

    /** Returns adventure-level progress for the authenticated child. */
    @GetMapping("/adventures")
    public ResponseEntity<ApiResponse<List<AdventureProgressResponse>>> getAdventureProgress(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<AdventureProgressResponse> response = progressService.getAdventureProgress(userId);
        return ResponseEntity.ok(ApiResponse.success("Adventure progress fetched successfully", response));
    }

    /** Returns vocabulary progress (all learned words) for the authenticated child. */
    @GetMapping("/vocabulary")
    public ResponseEntity<ApiResponse<List<VocabularyProgressResponse>>> getVocabularyProgress(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<VocabularyProgressResponse> response = progressService.getVocabularyProgress(userId);
        return ResponseEntity.ok(ApiResponse.success("Vocabulary progress fetched successfully", response));
    }

    /** Returns words due for spaced-repetition review. */
    @GetMapping("/vocabulary/due")
    public ResponseEntity<ApiResponse<List<VocabularyProgressResponse>>> getDueVocabulary(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        List<VocabularyProgressResponse> response = progressService.getDueVocabulary(userId);
        return ResponseEntity.ok(ApiResponse.success("Due vocabulary fetched successfully", response));
    }
}
