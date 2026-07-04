package com.exe.buddy_english_be.modules.mission.service;

import com.exe.buddy_english_be.modules.mission.dto.MissionRequest;
import com.exe.buddy_english_be.modules.mission.dto.MissionResponse;
import com.exe.buddy_english_be.modules.mission.entity.Mission;
import com.exe.buddy_english_be.modules.mission.repository.MissionRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MissionServiceImpl implements MissionService {
    private final MissionRepository missionRepository;

    public MissionServiceImpl(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MissionResponse> getAllMissions() {
        return missionRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MissionResponse getMissionById(Long id) {
        return toResponse(findMission(id));
    }

    @Override
    @Transactional
    public MissionResponse createMission(MissionRequest request) {
        Mission mission = Mission.builder()
                .title(request.title().trim())
                .description(normalize(request.description()))
                .missionFrequency(request.missionFrequency())
                .targetType(request.targetType())
                .targetValue(request.targetValue())
                .rewardCoin(request.rewardCoin())
                .rewardXp(request.rewardXp())
                .build();

        return toResponse(missionRepository.save(mission));
    }

    @Override
    @Transactional
    public MissionResponse updateMission(Long id, MissionRequest request) {
        Mission mission = findMission(id);
        mission.setTitle(request.title().trim());
        mission.setDescription(normalize(request.description()));
        mission.setMissionFrequency(request.missionFrequency());
        mission.setTargetType(request.targetType());
        mission.setTargetValue(request.targetValue());
        mission.setRewardCoin(request.rewardCoin());
        mission.setRewardXp(request.rewardXp());

        return toResponse(missionRepository.save(mission));
    }

    @Override
    @Transactional
    public void deleteMission(Long id) {
        Mission mission = findMission(id);
        missionRepository.delete(mission);
    }

    private Mission findMission(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MISSION_NOT_FOUND));
    }

    private MissionResponse toResponse(Mission mission) {
        return new MissionResponse(
                mission.getId(),
                mission.getTitle(),
                mission.getDescription(),
                mission.getMissionFrequency(),
                mission.getTargetType(),
                mission.getTargetValue(),
                mission.getRewardCoin(),
                mission.getRewardXp(),
                mission.getCreatedAt(),
                mission.getUpdatedAt()
        );
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
