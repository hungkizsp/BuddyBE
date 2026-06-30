package com.exe.buddy_english_be.modules.achievement.repository;

import com.exe.buddy_english_be.modules.achievement.entity.ChildAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildAchievementRepository extends JpaRepository<ChildAchievement, Long> {

    List<ChildAchievement> findByChildId(Long childId);

    boolean existsByChildIdAndAchievementId(Long childId, Long achievementId);
}
