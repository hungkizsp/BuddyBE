package com.exe.buddy_english_be.modules.learning.repository;

import com.exe.buddy_english_be.modules.learning.entity.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    List<Scenario> findByWorldIdOrderByOrderIndexAsc(Long worldId);
}
