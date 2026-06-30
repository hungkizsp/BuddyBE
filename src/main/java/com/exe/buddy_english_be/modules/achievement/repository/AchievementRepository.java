package com.exe.buddy_english_be.modules.achievement.repository;

import com.exe.buddy_english_be.modules.achievement.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
}
