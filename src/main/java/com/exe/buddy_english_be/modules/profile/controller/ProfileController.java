package com.exe.buddy_english_be.modules.profile.controller;

import com.exe.buddy_english_be.modules.profile.dto.ChildProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.CreateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.UpdateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.CreateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.ParentChildrenResponse;
import com.exe.buddy_english_be.modules.profile.dto.ParentProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.service.ProfileService;
import com.exe.buddy_english_be.shared.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController("modulesProfileController")
@RequestMapping("/api/profile")
@AllArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    // ─── Child endpoints ──────────────────────────────────────────────────────

    /** [Admin] Get all child profiles. */
    @GetMapping("/children")
    public ResponseEntity<ApiResponse<List<ChildProfileResponse>>> getAllChildProfiles() {
        List<ChildProfileResponse> response = profileService.getAllChildProfiles();
        return ResponseEntity.ok(ApiResponse.success("Child profiles fetched successfully", response));
    }

    /** [Admin] Get a child profile by profile ID. */
    @GetMapping("/children/{id}")
    public ResponseEntity<ApiResponse<ChildProfileResponse>> getChildProfileById(
            @PathVariable Long id) {
        ChildProfileResponse response = profileService.getChildProfileById(id);
        return ResponseEntity.ok(ApiResponse.success("Child profile fetched successfully", response));
    }

    /** [Self] Get the authenticated child's own profile. */
    @GetMapping("/child")
    public ResponseEntity<ApiResponse<ChildProfileResponse>> getChildProfile(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ChildProfileResponse response = profileService.getChildProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Child profile fetched successfully", response));
    }

    /** [Admin] Create a new child profile. */
    @PostMapping("/children")
    public ResponseEntity<ApiResponse<ChildProfileResponse>> createChildProfile(
            @Valid @RequestBody CreateChildProfileRequest request) {
        ChildProfileResponse response = profileService.createChildProfile(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Child profile created successfully", response));
    }

    /** [Self] Update the authenticated child's own profile. */
    @PutMapping("/child")
    public ResponseEntity<ApiResponse<ChildProfileResponse>> updateChildProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateChildProfileRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        ChildProfileResponse response = profileService.updateChildProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Child profile updated successfully", response));
    }

    /** [Admin] Update a child profile by profile ID. */
    @PutMapping("/children/{id}")
    public ResponseEntity<ApiResponse<ChildProfileResponse>> updateChildProfileById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChildProfileRequest request) {
        ChildProfileResponse response = profileService.updateChildProfileById(id, request);
        return ResponseEntity.ok(ApiResponse.success("Child profile updated successfully", response));
    }

    /** [Admin] Delete a child profile by profile ID. */
    @DeleteMapping("/children/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChildProfile(@PathVariable Long id) {
        profileService.deleteChildProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Child profile deleted successfully", null));
    }

    // ─── Parent endpoints ─────────────────────────────────────────────────────

    /** [Admin] Get all parent profiles. */
    @GetMapping("/parents")
    public ResponseEntity<ApiResponse<List<ParentProfileResponse>>> getAllParentProfiles() {
        List<ParentProfileResponse> response = profileService.getAllParentProfiles();
        return ResponseEntity.ok(ApiResponse.success("Parent profiles fetched successfully", response));
    }

    /** [Admin] Get a parent profile by profile ID. */
    @GetMapping("/parents/{id}")
    public ResponseEntity<ApiResponse<ParentProfileResponse>> getParentProfileById(
            @PathVariable Long id) {
        ParentProfileResponse response = profileService.getParentProfileById(id);
        return ResponseEntity.ok(ApiResponse.success("Parent profile fetched successfully", response));
    }

    /** [Self] Get the authenticated parent's own profile. */
    @GetMapping("/parent")
    public ResponseEntity<ApiResponse<ParentProfileResponse>> getParentProfile(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ParentProfileResponse response = profileService.getParentProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("Parent profile fetched successfully", response));
    }

    /** [Admin] Create a new parent profile. */
    @PostMapping("/parents")
    public ResponseEntity<ApiResponse<ParentProfileResponse>> createParentProfile(
            @Valid @RequestBody CreateParentProfileRequest request) {
        ParentProfileResponse response = profileService.createParentProfile(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Parent profile created successfully", response));
    }

    /** [Self] Update the authenticated parent's own profile. */
    @PutMapping("/parent")
    public ResponseEntity<ApiResponse<ParentProfileResponse>> updateParentProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateParentProfileRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        ParentProfileResponse response = profileService.updateParentProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Parent profile updated successfully", response));
    }

    /** [Admin] Update a parent profile by profile ID. */
    @PutMapping("/parents/{id}")
    public ResponseEntity<ApiResponse<ParentProfileResponse>> updateParentProfileById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateParentProfileRequest request) {
        ParentProfileResponse response = profileService.updateParentProfileById(id, request);
        return ResponseEntity.ok(ApiResponse.success("Parent profile updated successfully", response));
    }

    /** [Admin] Delete a parent profile by profile ID. */
    @DeleteMapping("/parents/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteParentProfile(@PathVariable Long id) {
        profileService.deleteParentProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Parent profile deleted successfully", null));
    }

    // ─── Parent–child endpoints ───────────────────────────────────────────────

    /** [Self] Get children of the authenticated parent. */
    @GetMapping("/parent/children")
    public ResponseEntity<ApiResponse<ParentChildrenResponse>> getChildrenOfParent(
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        ParentChildrenResponse response = profileService.getChildrenOfParent(userId);
        return ResponseEntity.ok(ApiResponse.success("Children fetched successfully", response));
    }

    /** [Admin] Get children of a parent by parent profile ID. */
    @GetMapping("/parents/{parentId}/children")
    public ResponseEntity<ApiResponse<ParentChildrenResponse>> getChildrenOfParentById(
            @PathVariable Long parentId) {
        ParentChildrenResponse response = profileService.getChildrenOfParentById(parentId);
        return ResponseEntity.ok(ApiResponse.success("Children fetched successfully", response));
    }
}
