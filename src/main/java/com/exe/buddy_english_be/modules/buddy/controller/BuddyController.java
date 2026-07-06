package com.exe.buddy_english_be.modules.buddy.controller;

import com.exe.buddy_english_be.modules.buddy.dto.*;
import com.exe.buddy_english_be.modules.buddy.service.*;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("modulesBuddyController")
@RequestMapping("/api/buddy")
@RequiredArgsConstructor
public class BuddyController {

    private final BuddyProfileService buddyProfileService;
    private final BuddyCustomizationService buddyCustomizationService;
    private final ChildCustomizationService childCustomizationService;
    private final ChildMemoryService childMemoryService;

    // ---- Buddy Profiles ----

    @PostMapping("/profiles")
    public ResponseEntity<ApiResponse<BuddyProfileResponse>> createBuddyProfile(
            @Valid @RequestBody CreateBuddyProfileRequest request) {
        BuddyProfileResponse response = buddyProfileService.createBuddyProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Buddy profile created successfully", response));
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<ApiResponse<BuddyProfileResponse>> getBuddyProfileById(@PathVariable Long id) {
        BuddyProfileResponse response = buddyProfileService.getBuddyProfileById(id);
        return ResponseEntity.ok(ApiResponse.success("Buddy profile fetched successfully", response));
    }

    @GetMapping("/profiles")
    public ResponseEntity<ApiResponse<List<BuddyProfileResponse>>> getAllBuddyProfiles() {
        List<BuddyProfileResponse> responses = buddyProfileService.getAllBuddyProfiles();
        return ResponseEntity.ok(ApiResponse.success("Buddy profiles fetched successfully", responses));
    }

    @PutMapping("/profiles/{id}")
    public ResponseEntity<ApiResponse<BuddyProfileResponse>> updateBuddyProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBuddyProfileRequest request) {
        BuddyProfileResponse response = buddyProfileService.updateBuddyProfile(id, request);
        return ResponseEntity.ok(ApiResponse.success("Buddy profile updated successfully", response));
    }

    @DeleteMapping("/profiles/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBuddyProfile(@PathVariable Long id) {
        buddyProfileService.deleteBuddyProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Buddy profile deleted successfully", null));
    }

    // ---- Buddy Customizations ----

    @PostMapping("/customizations")
    public ResponseEntity<ApiResponse<BuddyCustomizationResponse>> createBuddyCustomization(
            @Valid @RequestBody CreateBuddyCustomizationRequest request) {
        BuddyCustomizationResponse response = buddyCustomizationService.createBuddyCustomization(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Buddy customization created successfully", response));
    }

    @GetMapping("/customizations/{id}")
    public ResponseEntity<ApiResponse<BuddyCustomizationResponse>> getBuddyCustomizationById(@PathVariable Long id) {
        BuddyCustomizationResponse response = buddyCustomizationService.getBuddyCustomizationById(id);
        return ResponseEntity.ok(ApiResponse.success("Buddy customization fetched successfully", response));
    }

    @GetMapping("/customizations")
    public ResponseEntity<ApiResponse<List<BuddyCustomizationResponse>>> getAllBuddyCustomizations() {
        List<BuddyCustomizationResponse> responses = buddyCustomizationService.getAllBuddyCustomizations();
        return ResponseEntity.ok(ApiResponse.success("Buddy customizations fetched successfully", responses));
    }

    @PutMapping("/customizations/{id}")
    public ResponseEntity<ApiResponse<BuddyCustomizationResponse>> updateBuddyCustomization(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBuddyCustomizationRequest request) {
        BuddyCustomizationResponse response = buddyCustomizationService.updateBuddyCustomization(id, request);
        return ResponseEntity.ok(ApiResponse.success("Buddy customization updated successfully", response));
    }

    @DeleteMapping("/customizations/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBuddyCustomization(@PathVariable Long id) {
        buddyCustomizationService.deleteBuddyCustomization(id);
        return ResponseEntity.ok(ApiResponse.success("Buddy customization deleted successfully", null));
    }

    // ---- Child Customizations ----

    @PostMapping("/child-customizations")
    public ResponseEntity<ApiResponse<ChildCustomizationResponse>> createChildCustomization(
            @Valid @RequestBody CreateChildCustomizationRequest request) {
        ChildCustomizationResponse response = childCustomizationService.createChildCustomization(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Child customization created successfully", response));
    }

    @GetMapping("/child-customizations/{id}")
    public ResponseEntity<ApiResponse<ChildCustomizationResponse>> getChildCustomizationById(@PathVariable Long id) {
        ChildCustomizationResponse response = childCustomizationService.getChildCustomizationById(id);
        return ResponseEntity.ok(ApiResponse.success("Child customization fetched successfully", response));
    }

    @GetMapping("/child-customizations")
    public ResponseEntity<ApiResponse<List<ChildCustomizationResponse>>> getAllChildCustomizations() {
        List<ChildCustomizationResponse> responses = childCustomizationService.getAllChildCustomizations();
        return ResponseEntity.ok(ApiResponse.success("Child customizations fetched successfully", responses));
    }

    @PutMapping("/child-customizations/{id}")
    public ResponseEntity<ApiResponse<ChildCustomizationResponse>> updateChildCustomization(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChildCustomizationRequest request) {
        ChildCustomizationResponse response = childCustomizationService.updateChildCustomization(id, request);
        return ResponseEntity.ok(ApiResponse.success("Child customization updated successfully", response));
    }

    @DeleteMapping("/child-customizations/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChildCustomization(@PathVariable Long id) {
        childCustomizationService.deleteChildCustomization(id);
        return ResponseEntity.ok(ApiResponse.success("Child customization deleted successfully", null));
    }

    // ---- Child Memories ----

    @PostMapping("/child-memories")
    public ResponseEntity<ApiResponse<ChildMemoryResponse>> createChildMemory(
            @Valid @RequestBody CreateChildMemoryRequest request) {
        ChildMemoryResponse response = childMemoryService.createChildMemory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Child memory created successfully", response));
    }

    @GetMapping("/child-memories/{id}")
    public ResponseEntity<ApiResponse<ChildMemoryResponse>> getChildMemoryById(@PathVariable Long id) {
        ChildMemoryResponse response = childMemoryService.getChildMemoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Child memory fetched successfully", response));
    }

    @GetMapping("/child-memories")
    public ResponseEntity<ApiResponse<List<ChildMemoryResponse>>> getAllChildMemories() {
        List<ChildMemoryResponse> responses = childMemoryService.getAllChildMemories();
        return ResponseEntity.ok(ApiResponse.success("Child memories fetched successfully", responses));
    }

    @PutMapping("/child-memories/{id}")
    public ResponseEntity<ApiResponse<ChildMemoryResponse>> updateChildMemory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateChildMemoryRequest request) {
        ChildMemoryResponse response = childMemoryService.updateChildMemory(id, request);
        return ResponseEntity.ok(ApiResponse.success("Child memory updated successfully", response));
    }

    @DeleteMapping("/child-memories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteChildMemory(@PathVariable Long id) {
        childMemoryService.deleteChildMemory(id);
        return ResponseEntity.ok(ApiResponse.success("Child memory deleted successfully", null));
    }
}
