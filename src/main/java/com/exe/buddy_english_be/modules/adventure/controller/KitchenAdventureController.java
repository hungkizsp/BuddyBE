package com.exe.buddy_english_be.modules.adventure.controller;

import com.exe.buddy_english_be.modules.adventure.dto.BuddyPositionDto;
import com.exe.buddy_english_be.modules.adventure.dto.CombineItemsRequest;
import com.exe.buddy_english_be.modules.adventure.dto.DropItemRequest;
import com.exe.buddy_english_be.modules.adventure.dto.KitchenAdventureActionResponse;
import com.exe.buddy_english_be.modules.adventure.dto.KitchenAdventureStateResponse;
import com.exe.buddy_english_be.modules.adventure.dto.ToggleStateRequest;
import com.exe.buddy_english_be.modules.adventure.service.KitchenAdventureService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("modulesKitchenAdventureController")
@RequestMapping("/api/adventures/kitchen")
public class KitchenAdventureController {
    private final KitchenAdventureService kitchenAdventureService;

    public KitchenAdventureController(KitchenAdventureService kitchenAdventureService) {
        this.kitchenAdventureService = kitchenAdventureService;
    }

    @GetMapping("/state")
    public ResponseEntity<ApiResponse<KitchenAdventureStateResponse>> getState(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureStateResponse response = kitchenAdventureService.getState(userId);
        return ResponseEntity.ok(ApiResponse.success("Kitchen adventure state fetched successfully", response));
    }

    @PostMapping("/start")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> startMission(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.startMission(userId);
        return ResponseEntity.ok(ApiResponse.success("Kitchen mission started successfully", response));
    }

    @PostMapping("/arrive")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> arriveAtTable(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.arriveAtTable(userId);
        return ResponseEntity.ok(ApiResponse.success("Buddy arrival handled successfully", response));
    }

    @PostMapping("/drop-on-buddy")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> dropOnBuddy(
            Authentication authentication,
            @Valid @RequestBody DropItemRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.dropOnBuddy(userId, request.itemId());
        return ResponseEntity.ok(ApiResponse.success("Buddy drop handled successfully", response));
    }

    @PostMapping("/combine")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> combineItems(
            Authentication authentication,
            @Valid @RequestBody CombineItemsRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.combineItems(
                userId,
                request.draggedId(),
                request.targetId()
        );
        return ResponseEntity.ok(ApiResponse.success("Kitchen item combination handled successfully", response));
    }

    @PostMapping("/drop-on-pot")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> dropOnPot(
            Authentication authentication,
            @Valid @RequestBody DropItemRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.dropOnPot(userId, request.itemId());
        return ResponseEntity.ok(ApiResponse.success("Pot drop handled successfully", response));
    }

    @PatchMapping("/buddy-position")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> updateBuddyPosition(
            Authentication authentication,
            @Valid @RequestBody BuddyPositionDto request) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.updateBuddyPosition(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Buddy position updated successfully", response));
    }

    @PatchMapping("/mic")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> toggleMic(
            Authentication authentication,
            @RequestBody(required = false) ToggleStateRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.toggleMic(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Microphone state updated successfully", response));
    }

    @PatchMapping("/mission-panel")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> toggleMissionPanel(
            Authentication authentication,
            @RequestBody(required = false) ToggleStateRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.toggleMissionPanel(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Mission panel state updated successfully", response));
    }

    @PostMapping("/rewards/close")
    public ResponseEntity<ApiResponse<KitchenAdventureActionResponse>> closeRewards(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        KitchenAdventureActionResponse response = kitchenAdventureService.closeRewards(userId);
        return ResponseEntity.ok(ApiResponse.success("Reward popup closed successfully", response));
    }
}
