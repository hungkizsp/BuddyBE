package com.exe.buddy_english_be.modules.progress.controller;

import com.exe.buddy_english_be.modules.progress.dto.*;
import com.exe.buddy_english_be.modules.progress.service.ProgressService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController("modulesProgressController")
@RequestMapping("/api/progress")
public class ProgressController {
    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    // Vocabulary Progress Endpoints

    @GetMapping("/vocabularies")
    public ResponseEntity<ApiResponse<List<ChildVocabularyProgressResponse>>> getVocabularyProgressByChildId(
            @RequestParam @NotNull(message = "childId is required") Long childId) {
        List<ChildVocabularyProgressResponse> response = progressService.getVocabularyProgressByChildId(childId);
        return ResponseEntity.ok(ApiResponse.success("Vocabulary progress fetched successfully", response));
    }

    @GetMapping("/vocabularies/due")
    public ResponseEntity<ApiResponse<List<ChildVocabularyProgressResponse>>> getDueVocabularyProgress(
            @RequestParam @NotNull(message = "childId is required") Long childId) {
        List<ChildVocabularyProgressResponse> response = progressService.getDueVocabularyProgress(childId);
        return ResponseEntity.ok(ApiResponse.success("Due vocabulary progress fetched successfully", response));
    }

    @GetMapping("/vocabularies/{id}")
    public ResponseEntity<ApiResponse<ChildVocabularyProgressResponse>> getVocabularyProgressById(
            @PathVariable Long id) {
        ChildVocabularyProgressResponse response = progressService.getVocabularyProgressById(id);
        return ResponseEntity.ok(ApiResponse.success("Vocabulary progress fetched successfully", response));
    }

    @PostMapping("/vocabularies")
    public ResponseEntity<ApiResponse<ChildVocabularyProgressResponse>> createVocabularyProgress(
            @Valid @RequestBody ChildVocabularyProgressRequest request) {
        ChildVocabularyProgressResponse response = progressService.createVocabularyProgress(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vocabulary progress created successfully", response));
    }

    @PutMapping("/vocabularies/{id}")
    public ResponseEntity<ApiResponse<ChildVocabularyProgressResponse>> updateVocabularyProgress(
            @PathVariable Long id,
            @Valid @RequestBody ChildVocabularyProgressRequest request) {
        ChildVocabularyProgressResponse response = progressService.updateVocabularyProgress(id, request);
        return ResponseEntity.ok(ApiResponse.success("Vocabulary progress updated successfully", response));
    }

    @DeleteMapping("/vocabularies/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteVocabularyProgress(@PathVariable Long id) {
        progressService.deleteVocabularyProgress(id);
        return ResponseEntity.ok(ApiResponse.success("Vocabulary progress deleted successfully", null));
    }

    // World Progress Endpoints

    @GetMapping("/worlds")
    public ResponseEntity<ApiResponse<List<ChildWorldProgressResponse>>> getWorldProgressByChildId(
            @RequestParam @NotNull(message = "childId is required") Long childId) {
        List<ChildWorldProgressResponse> response = progressService.getWorldProgressByChildId(childId);
        return ResponseEntity.ok(ApiResponse.success("World progress fetched successfully", response));
    }

    @GetMapping("/worlds/{id}")
    public ResponseEntity<ApiResponse<ChildWorldProgressResponse>> getWorldProgressById(@PathVariable Long id) {
        ChildWorldProgressResponse response = progressService.getWorldProgressById(id);
        return ResponseEntity.ok(ApiResponse.success("World progress fetched successfully", response));
    }

    @PostMapping("/worlds")
    public ResponseEntity<ApiResponse<ChildWorldProgressResponse>> createWorldProgress(
            @Valid @RequestBody ChildWorldProgressRequest request) {
        ChildWorldProgressResponse response = progressService.createWorldProgress(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("World progress created successfully", response));
    }

    @PutMapping("/worlds/{id}")
    public ResponseEntity<ApiResponse<ChildWorldProgressResponse>> updateWorldProgress(
            @PathVariable Long id,
            @Valid @RequestBody ChildWorldProgressRequest request) {
        ChildWorldProgressResponse response = progressService.updateWorldProgress(id, request);
        return ResponseEntity.ok(ApiResponse.success("World progress updated successfully", response));
    }

    @DeleteMapping("/worlds/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorldProgress(@PathVariable Long id) {
        progressService.deleteWorldProgress(id);
        return ResponseEntity.ok(ApiResponse.success("World progress deleted successfully", null));
    }

    // Scenario Progress Endpoints

    @GetMapping("/scenarios")
    public ResponseEntity<ApiResponse<List<ChildScenarioProgressResponse>>> getScenarioProgressByChildId(
            @RequestParam @NotNull(message = "childId is required") Long childId) {
        List<ChildScenarioProgressResponse> response = progressService.getScenarioProgressByChildId(childId);
        return ResponseEntity.ok(ApiResponse.success("Scenario progress fetched successfully", response));
    }

    @GetMapping("/scenarios/{id}")
    public ResponseEntity<ApiResponse<ChildScenarioProgressResponse>> getScenarioProgressById(@PathVariable Long id) {
        ChildScenarioProgressResponse response = progressService.getScenarioProgressById(id);
        return ResponseEntity.ok(ApiResponse.success("Scenario progress fetched successfully", response));
    }

    @PostMapping("/scenarios")
    public ResponseEntity<ApiResponse<ChildScenarioProgressResponse>> createScenarioProgress(
            @Valid @RequestBody ChildScenarioProgressRequest request) {
        ChildScenarioProgressResponse response = progressService.createScenarioProgress(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Scenario progress created successfully", response));
    }

    @PutMapping("/scenarios/{id}")
    public ResponseEntity<ApiResponse<ChildScenarioProgressResponse>> updateScenarioProgress(
            @PathVariable Long id,
            @Valid @RequestBody ChildScenarioProgressRequest request) {
        ChildScenarioProgressResponse response = progressService.updateScenarioProgress(id, request);
        return ResponseEntity.ok(ApiResponse.success("Scenario progress updated successfully", response));
    }

    @DeleteMapping("/scenarios/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScenarioProgress(@PathVariable Long id) {
        progressService.deleteScenarioProgress(id);
        return ResponseEntity.ok(ApiResponse.success("Scenario progress deleted successfully", null));
    }

    // Complete Scenario (atomic — updates scenario + vocabulary + world progress)

    @PostMapping("/scenarios/complete")
    public ResponseEntity<ApiResponse<CompleteScenarioResponse>> completeScenario(
            @Valid @RequestBody CompleteScenarioRequest request) {
        CompleteScenarioResponse response = progressService.completeScenario(request);
        return ResponseEntity.ok(ApiResponse.success("Scenario completed successfully", response));
    }

}
