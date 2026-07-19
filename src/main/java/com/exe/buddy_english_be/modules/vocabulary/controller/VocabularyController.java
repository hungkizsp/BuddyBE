package com.exe.buddy_english_be.modules.vocabulary.controller;

import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyRequest;
import com.exe.buddy_english_be.modules.vocabulary.dto.VocabularyResponse;
import com.exe.buddy_english_be.modules.vocabulary.service.VocabularyService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("modulesVocabularyController")
@RequestMapping("/api/vocabularies")
public class VocabularyController {
    private final VocabularyService vocabularyService;

    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VocabularyResponse>>> getAllVocabularies(
            @RequestParam(required = false) Long categoryId) {
        List<VocabularyResponse> response = categoryId == null
                ? vocabularyService.getAllVocabularies()
                : vocabularyService.getVocabulariesByCategoryId(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Vocabularies fetched successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VocabularyResponse>> getVocabularyById(@PathVariable Long id) {
        VocabularyResponse response = vocabularyService.getVocabularyById(id);
        return ResponseEntity.ok(ApiResponse.success("Vocabulary fetched successfully", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<VocabularyResponse>> createVocabulary(
            @Valid @RequestBody VocabularyRequest request) {
        VocabularyResponse response = vocabularyService.createVocabulary(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vocabulary created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<VocabularyResponse>> updateVocabulary(
            @PathVariable Long id,
            @Valid @RequestBody VocabularyRequest request) {
        VocabularyResponse response = vocabularyService.updateVocabulary(id, request);
        return ResponseEntity.ok(ApiResponse.success("Vocabulary updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVocabulary(@PathVariable Long id) {
        vocabularyService.deleteVocabulary(id);
        return ResponseEntity.ok(ApiResponse.success("Vocabulary deleted successfully", null));
    }
}
