package com.exe.buddy_english_be.modules.reward.controller;

import com.exe.buddy_english_be.modules.reward.dto.*;
import com.exe.buddy_english_be.modules.reward.service.*;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("modulesRewardController")
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    private final ChildRewardService childRewardService;

    // ---- Rewards ----

    @PostMapping
    public ResponseEntity<ApiResponse<RewardResponse>> createReward(@Valid @RequestBody CreateRewardRequest request) {
        RewardResponse response = rewardService.createReward(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reward created successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RewardResponse>> getRewardById(@PathVariable Long id) {
        RewardResponse response = rewardService.getRewardById(id);
        return ResponseEntity.ok(ApiResponse.success("Reward fetched successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RewardResponse>>> getAllRewards() {
        List<RewardResponse> responses = rewardService.getAllRewards();
        return ResponseEntity.ok(ApiResponse.success("Rewards fetched successfully", responses));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RewardResponse>> updateReward(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRewardRequest request) {
        RewardResponse response = rewardService.updateReward(id, request);
        return ResponseEntity.ok(ApiResponse.success("Reward updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReward(@PathVariable Long id) {
        rewardService.deleteReward(id);
        return ResponseEntity.ok(ApiResponse.success("Reward deleted successfully", null));
    }

    // ---- Child Rewards ----

    @PostMapping("/child")
    public ResponseEntity<ApiResponse<ChildRewardResponse>> createChildReward(
            @Valid @RequestBody CreateChildRewardRequest request) {
        ChildRewardResponse response = childRewardService.createChildReward(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Child reward created successfully", response));
    }

    @GetMapping("/child/{id}")
    public ResponseEntity<ApiResponse<ChildRewardResponse>> getChildRewardById(@PathVariable Long id) {
        ChildRewardResponse response = childRewardService.getChildRewardById(id);
        return ResponseEntity.ok(ApiResponse.success("Child reward fetched successfully", response));
    }

    @GetMapping("/child")
    public ResponseEntity<ApiResponse<List<ChildRewardResponse>>> getAllChildRewards() {
        List<ChildRewardResponse> responses = childRewardService.getAllChildRewards();
        return ResponseEntity.ok(ApiResponse.success("Child rewards fetched successfully", responses));
    }

    @PutMapping("/child/{id}")
    public ResponseEntity<ApiResponse<ChildRewardResponse>> updateChildReward(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChildRewardRequest request) {
        ChildRewardResponse response = childRewardService.updateChildReward(id, request);
        return ResponseEntity.ok(ApiResponse.success("Child reward updated successfully", response));
    }

    @DeleteMapping("/child/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChildReward(@PathVariable Long id) {
        childRewardService.deleteChildReward(id);
        return ResponseEntity.ok(ApiResponse.success("Child reward deleted successfully", null));
    }
}
