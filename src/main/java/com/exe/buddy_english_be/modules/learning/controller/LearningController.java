package com.exe.buddy_english_be.modules.learning.controller;

import com.exe.buddy_english_be.modules.learning.dto.ScenarioRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioResponse;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioStepRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioStepResponse;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioVocabularyRequest;
import com.exe.buddy_english_be.modules.learning.dto.ScenarioVocabularyResponse;
import com.exe.buddy_english_be.modules.learning.dto.WorldRequest;
import com.exe.buddy_english_be.modules.learning.dto.WorldResponse;
import com.exe.buddy_english_be.modules.learning.service.LearningService;
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

@RestController("modulesLearningController")
@RequestMapping("/api/learning")
public class LearningController {
    private final LearningService learningService;

    public LearningController(LearningService learningService) {
        this.learningService = learningService;
    }

    @GetMapping("/worlds")
    public ResponseEntity<ApiResponse<List<WorldResponse>>> getAllWorlds(
            @RequestParam(required = false) Boolean activeOnly) {
        List<WorldResponse> response = Boolean.TRUE.equals(activeOnly)
                ? learningService.getActiveWorlds()
                : learningService.getAllWorlds();
        return ResponseEntity.ok(ApiResponse.success("Worlds fetched successfully", response));
    }

    @GetMapping("/worlds/{id}")
    public ResponseEntity<ApiResponse<WorldResponse>> getWorldById(@PathVariable Long id) {
        WorldResponse response = learningService.getWorldById(id);
        return ResponseEntity.ok(ApiResponse.success("World fetched successfully", response));
    }

    @PostMapping("/worlds")
    public ResponseEntity<ApiResponse<WorldResponse>> createWorld(@Valid @RequestBody WorldRequest request) {
        WorldResponse response = learningService.createWorld(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("World created successfully", response));
    }

    @PutMapping("/worlds/{id}")
    public ResponseEntity<ApiResponse<WorldResponse>> updateWorld(
            @PathVariable Long id,
            @Valid @RequestBody WorldRequest request) {
        WorldResponse response = learningService.updateWorld(id, request);
        return ResponseEntity.ok(ApiResponse.success("World updated successfully", response));
    }

    @DeleteMapping("/worlds/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteWorld(@PathVariable Long id) {
        learningService.deleteWorld(id);
        return ResponseEntity.ok(ApiResponse.success("World deleted successfully", null));
    }

    @GetMapping("/scenarios")
    public ResponseEntity<ApiResponse<List<ScenarioResponse>>> getAllScenarios(
            @RequestParam(required = false) Long worldId) {
        List<ScenarioResponse> response = worldId == null
                ? learningService.getAllScenarios()
                : learningService.getScenariosByWorldId(worldId);
        return ResponseEntity.ok(ApiResponse.success("Scenarios fetched successfully", response));
    }

    @GetMapping("/scenarios/{id}")
    public ResponseEntity<ApiResponse<ScenarioResponse>> getScenarioById(@PathVariable Long id) {
        ScenarioResponse response = learningService.getScenarioById(id);
        return ResponseEntity.ok(ApiResponse.success("Scenario fetched successfully", response));
    }

    @PostMapping("/scenarios")
    public ResponseEntity<ApiResponse<ScenarioResponse>> createScenario(
            @Valid @RequestBody ScenarioRequest request) {
        ScenarioResponse response = learningService.createScenario(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Scenario created successfully", response));
    }

    @PutMapping("/scenarios/{id}")
    public ResponseEntity<ApiResponse<ScenarioResponse>> updateScenario(
            @PathVariable Long id,
            @Valid @RequestBody ScenarioRequest request) {
        ScenarioResponse response = learningService.updateScenario(id, request);
        return ResponseEntity.ok(ApiResponse.success("Scenario updated successfully", response));
    }

    @DeleteMapping("/scenarios/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScenario(@PathVariable Long id) {
        learningService.deleteScenario(id);
        return ResponseEntity.ok(ApiResponse.success("Scenario deleted successfully", null));
    }

    @GetMapping("/scenario-steps")
    public ResponseEntity<ApiResponse<List<ScenarioStepResponse>>> getAllScenarioSteps(
            @RequestParam(required = false) Long scenarioId) {
        List<ScenarioStepResponse> response = scenarioId == null
                ? learningService.getAllScenarioSteps()
                : learningService.getScenarioStepsByScenarioId(scenarioId);
        return ResponseEntity.ok(ApiResponse.success("Scenario steps fetched successfully", response));
    }

    @GetMapping("/scenario-steps/{id}")
    public ResponseEntity<ApiResponse<ScenarioStepResponse>> getScenarioStepById(@PathVariable Long id) {
        ScenarioStepResponse response = learningService.getScenarioStepById(id);
        return ResponseEntity.ok(ApiResponse.success("Scenario step fetched successfully", response));
    }

    @PostMapping("/scenario-steps")
    public ResponseEntity<ApiResponse<ScenarioStepResponse>> createScenarioStep(
            @Valid @RequestBody ScenarioStepRequest request) {
        ScenarioStepResponse response = learningService.createScenarioStep(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Scenario step created successfully", response));
    }

    @PutMapping("/scenario-steps/{id}")
    public ResponseEntity<ApiResponse<ScenarioStepResponse>> updateScenarioStep(
            @PathVariable Long id,
            @Valid @RequestBody ScenarioStepRequest request) {
        ScenarioStepResponse response = learningService.updateScenarioStep(id, request);
        return ResponseEntity.ok(ApiResponse.success("Scenario step updated successfully", response));
    }

    @DeleteMapping("/scenario-steps/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScenarioStep(@PathVariable Long id) {
        learningService.deleteScenarioStep(id);
        return ResponseEntity.ok(ApiResponse.success("Scenario step deleted successfully", null));
    }

    @GetMapping("/scenario-vocabularies")
    public ResponseEntity<ApiResponse<List<ScenarioVocabularyResponse>>> getAllScenarioVocabularies(
            @RequestParam(required = false) Long scenarioId) {
        List<ScenarioVocabularyResponse> response = scenarioId == null
                ? learningService.getAllScenarioVocabularies()
                : learningService.getScenarioVocabulariesByScenarioId(scenarioId);
        return ResponseEntity.ok(ApiResponse.success("Scenario vocabularies fetched successfully", response));
    }

    @GetMapping("/scenario-vocabularies/{id}")
    public ResponseEntity<ApiResponse<ScenarioVocabularyResponse>> getScenarioVocabularyById(@PathVariable Long id) {
        ScenarioVocabularyResponse response = learningService.getScenarioVocabularyById(id);
        return ResponseEntity.ok(ApiResponse.success("Scenario vocabulary fetched successfully", response));
    }

    @PostMapping("/scenario-vocabularies")
    public ResponseEntity<ApiResponse<ScenarioVocabularyResponse>> createScenarioVocabulary(
            @Valid @RequestBody ScenarioVocabularyRequest request) {
        ScenarioVocabularyResponse response = learningService.createScenarioVocabulary(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Scenario vocabulary created successfully", response));
    }

    @DeleteMapping("/scenario-vocabularies/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteScenarioVocabulary(@PathVariable Long id) {
        learningService.deleteScenarioVocabulary(id);
        return ResponseEntity.ok(ApiResponse.success("Scenario vocabulary deleted successfully", null));
    }
}
