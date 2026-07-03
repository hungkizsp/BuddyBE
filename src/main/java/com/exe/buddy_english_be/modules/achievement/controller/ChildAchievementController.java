package com.exe.buddy_english_be.modules.achievement.controller;

import com.exe.buddy_english_be.modules.achievement.dto.ChildAchievementRequest;
import com.exe.buddy_english_be.modules.achievement.dto.ChildAchievementResponse;
import com.exe.buddy_english_be.modules.achievement.service.AchievementService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

@Validated
@RestController("modulesChildAchievementController")
@RequestMapping("/api/child-achievements")
public class ChildAchievementController {
    private final AchievementService achievementService;

    public ChildAchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ChildAchievementResponse>>> getChildAchievements(
            @RequestParam @NotNull(message = "childId is required") Long childId) {
        List<ChildAchievementResponse> response = achievementService.getChildAchievements(childId);
        return ResponseEntity.ok(ApiResponse.success("Child achievements fetched successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChildAchievementResponse>> getChildAchievementById(@PathVariable Long id) {
        ChildAchievementResponse response = achievementService.getChildAchievementById(id);
        return ResponseEntity.ok(ApiResponse.success("Child achievement fetched successfully", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChildAchievementResponse>> createChildAchievement(
            @Valid @RequestBody ChildAchievementRequest request) {
        ChildAchievementResponse response = achievementService.createChildAchievement(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Child achievement created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChildAchievementResponse>> updateChildAchievement(
            @PathVariable Long id,
            @Valid @RequestBody ChildAchievementRequest request) {
        ChildAchievementResponse response = achievementService.updateChildAchievement(id, request);
        return ResponseEntity.ok(ApiResponse.success("Child achievement updated successfully", response));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ChildAchievementResponse>> upsertChildAchievement(
            @Valid @RequestBody ChildAchievementRequest request) {
        ChildAchievementResponse response = achievementService.upsertChildAchievement(request);
        return ResponseEntity.ok(ApiResponse.success("Child achievement saved successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChildAchievement(@PathVariable Long id) {
        achievementService.deleteChildAchievement(id);
        return ResponseEntity.ok(ApiResponse.success("Child achievement deleted successfully", null));
    }
}
