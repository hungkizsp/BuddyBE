package com.exe.buddy_english_be.modules.learning.repository;

import com.exe.buddy_english_be.modules.learning.entity.ScenarioStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioStepRepository extends JpaRepository<ScenarioStep, Long> {

    List<ScenarioStep> findByScenarioIdOrderByStepOrderAsc(Long scenarioId);
}
