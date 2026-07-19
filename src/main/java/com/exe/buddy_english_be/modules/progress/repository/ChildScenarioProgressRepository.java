package com.exe.buddy_english_be.modules.progress.repository;

import com.exe.buddy_english_be.modules.progress.entity.ChildScenarioProgress;
import com.exe.buddy_english_be.modules.progress.enums.ProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildScenarioProgressRepository extends JpaRepository<ChildScenarioProgress, Long> {

    List<ChildScenarioProgress> findByChildId(Long childId);

    Optional<ChildScenarioProgress> findByChildIdAndScenarioId(Long childId, Long scenarioId);

    long countByChildIdAndScenario_WorldIdAndStatus(Long childId, Long worldId, ProgressStatus status);
}
