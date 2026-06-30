package com.exe.buddy_english_be.modules.analytics.repository;

import com.exe.buddy_english_be.modules.analytics.entity.LearningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningSessionRepository extends JpaRepository<LearningSession, Long> {

    List<LearningSession> findByChildIdOrderByStartTimeDesc(Long childId);
}
