package com.exe.buddy_english_be.modules.mission.repository;

import com.exe.buddy_english_be.modules.mission.entity.ChildMission;
import com.exe.buddy_english_be.modules.mission.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildMissionRepository extends JpaRepository<ChildMission, Long> {

    List<ChildMission> findByChildId(Long childId);

    List<ChildMission> findByChildIdAndStatus(Long childId, MissionStatus status);
}
