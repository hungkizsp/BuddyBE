package com.exe.buddy_english_be.modules.mission.service;

import com.exe.buddy_english_be.modules.mission.dto.MissionRequest;
import com.exe.buddy_english_be.modules.mission.dto.MissionResponse;

import java.util.List;

public interface MissionService {

    List<MissionResponse> getAllMissions();

    MissionResponse getMissionById(Long id);

    MissionResponse createMission(MissionRequest request);

    MissionResponse updateMission(Long id, MissionRequest request);

    void deleteMission(Long id);
}
