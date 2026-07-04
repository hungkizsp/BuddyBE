package com.exe.buddy_english_be.modules.mission.controller;

import com.exe.buddy_english_be.modules.mission.dto.MissionRequest;
import com.exe.buddy_english_be.modules.mission.dto.MissionResponse;
import com.exe.buddy_english_be.modules.mission.service.MissionService;
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

@RestController("modulesMissionController")
@RequestMapping("/api/missions")
public class MissionController {
    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MissionResponse>>> getAllMissions() {
        List<MissionResponse> response = missionService.getAllMissions();
        return ResponseEntity.ok(ApiResponse.success("Missions fetched successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MissionResponse>> getMissionById(@PathVariable Long id) {
        MissionResponse response = missionService.getMissionById(id);
        return ResponseEntity.ok(ApiResponse.success("Mission fetched successfully", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MissionResponse>> createMission(
            @Valid @RequestBody MissionRequest request) {
        MissionResponse response = missionService.createMission(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mission created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MissionResponse>> updateMission(
            @PathVariable Long id,
            @Valid @RequestBody MissionRequest request) {
        MissionResponse response = missionService.updateMission(id, request);
        return ResponseEntity.ok(ApiResponse.success("Mission updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.ok(ApiResponse.success("Mission deleted successfully", null));
    }
}
