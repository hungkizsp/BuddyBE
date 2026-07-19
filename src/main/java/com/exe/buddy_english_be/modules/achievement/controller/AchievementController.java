package com.exe.buddy_english_be.modules.achievement.controller;

import com.exe.buddy_english_be.modules.achievement.dto.AchievementRequest;
import com.exe.buddy_english_be.modules.achievement.dto.AchievementResponse;
import com.exe.buddy_english_be.modules.achievement.service.AchievementService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("modulesAchievementController")
@RequestMapping("/api/achievements")
public class AchievementController {
    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AchievementResponse>>> getAllAchievements() {
        List<AchievementResponse> response = achievementService.getAllAchievements();
        return ResponseEntity.ok(ApiResponse.success("Achievements fetched successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AchievementResponse>> getAchievementById(@PathVariable Long id) {
        AchievementResponse response = achievementService.getAchievementById(id);
        return ResponseEntity.ok(ApiResponse.success("Achievement fetched successfully", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AchievementResponse>> createAchievement(
            @Valid @RequestBody AchievementRequest request) {
        AchievementResponse response = achievementService.createAchievement(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Achievement created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AchievementResponse>> updateAchievement(
            @PathVariable Long id,
            @Valid @RequestBody AchievementRequest request) {
        AchievementResponse response = achievementService.updateAchievement(id, request);
        return ResponseEntity.ok(ApiResponse.success("Achievement updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.ok(ApiResponse.success("Achievement deleted successfully", null));
    }
}
