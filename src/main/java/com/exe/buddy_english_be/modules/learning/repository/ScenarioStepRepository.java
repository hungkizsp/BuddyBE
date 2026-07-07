package com.exe.buddy_english_be.modules.learning.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exe.buddy_english_be.modules.learning.entity.ScenarioStep;

@Repository
public interface ScenarioStepRepository extends JpaRepository<ScenarioStep, Long> {

    List<ScenarioStep> findByScenarioIdOrderByStepOrderAsc(Long scenarioId);

    Optional<ScenarioStep> findByScenarioIdAndStepOrder(Long scenarioId, Integer stepOrder);
}
