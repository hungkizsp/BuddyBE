package com.exe.buddy_english_be.modules.progress.repository;

import com.exe.buddy_english_be.modules.progress.entity.ChildScenarioProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildScenarioProgressRepository extends JpaRepository<ChildScenarioProgress, Long> {
}
