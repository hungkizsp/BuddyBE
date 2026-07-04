package com.exe.buddy_english_be.modules.achievement.service;

import com.exe.buddy_english_be.modules.achievement.dto.AchievementRequest;
import com.exe.buddy_english_be.modules.achievement.dto.AchievementResponse;
import com.exe.buddy_english_be.modules.achievement.dto.ChildAchievementRequest;
import com.exe.buddy_english_be.modules.achievement.dto.ChildAchievementResponse;

import java.util.List;

public interface AchievementService {

    List<AchievementResponse> getAllAchievements();

    AchievementResponse getAchievementById(Long id);

    AchievementResponse createAchievement(AchievementRequest request);

    AchievementResponse updateAchievement(Long id, AchievementRequest request);

    void deleteAchievement(Long id);

    List<ChildAchievementResponse> getChildAchievements(Long childId);

    ChildAchievementResponse getChildAchievementById(Long id);

    ChildAchievementResponse createChildAchievement(ChildAchievementRequest request);

    ChildAchievementResponse updateChildAchievement(Long id, ChildAchievementRequest request);

    ChildAchievementResponse upsertChildAchievement(ChildAchievementRequest request);

    void deleteChildAchievement(Long id);
}
