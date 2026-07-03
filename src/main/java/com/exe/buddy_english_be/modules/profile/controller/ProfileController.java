package com.exe.buddy_english_be.modules.profile.controller;

import com.exe.buddy_english_be.modules.profile.dto.ChildProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.ParentChildrenResponse;
import com.exe.buddy_english_be.modules.profile.dto.ParentProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.service.ProfileService;
import com.exe.buddy_english_be.shared.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // ─── Child endpoints ──────────────────────────────────────────────────────

    @GetMapping("/child")
    public ResponseEntity<ApiResponse<ChildProfileResponse>> getChildProfile(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ChildProfileResponse response = profileService.getChildProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Child profile fetched successfully", response));
    }

    @PutMapping("/child")
    public ResponseEntity<ApiResponse<ChildProfileResponse>> updateChildProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateChildProfileRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        ChildProfileResponse response = profileService.updateChildProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Child profile updated successfully", response));
    }

    // ─── Parent endpoints ─────────────────────────────────────────────────────

    @GetMapping("/parent")
    public ResponseEntity<ApiResponse<ParentProfileResponse>> getParentProfile(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ParentProfileResponse response = profileService.getParentProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Parent profile fetched successfully", response));
    }

    @PutMapping("/parent")
    public ResponseEntity<ApiResponse<ParentProfileResponse>> updateParentProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateParentProfileRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        ParentProfileResponse response = profileService.updateParentProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Parent profile updated successfully", response));
    }

    // ─── Parent–child endpoints ───────────────────────────────────────────────

    @GetMapping("/parent/children")
    public ResponseEntity<ApiResponse<ParentChildrenResponse>> getChildrenOfParent(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ParentChildrenResponse response = profileService.getChildrenOfParent(userId);
        return ResponseEntity.ok(ApiResponse.success("Children fetched successfully", response));
    }
}
