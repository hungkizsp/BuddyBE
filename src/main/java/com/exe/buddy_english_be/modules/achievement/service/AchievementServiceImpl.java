package com.exe.buddy_english_be.modules.achievement.service;

import com.exe.buddy_english_be.modules.achievement.dto.AchievementRequest;
import com.exe.buddy_english_be.modules.achievement.dto.AchievementResponse;
import com.exe.buddy_english_be.modules.achievement.dto.ChildAchievementRequest;
import com.exe.buddy_english_be.modules.achievement.dto.ChildAchievementResponse;
import com.exe.buddy_english_be.modules.achievement.entity.Achievement;
import com.exe.buddy_english_be.modules.achievement.entity.ChildAchievement;
import com.exe.buddy_english_be.modules.achievement.repository.AchievementRepository;
import com.exe.buddy_english_be.modules.achievement.repository.ChildAchievementRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final ChildAchievementRepository childAchievementRepository;
    private final ChildProfileRepository childProfileRepository;

    public AchievementServiceImpl(
            AchievementRepository achievementRepository,
            ChildAchievementRepository childAchievementRepository,
            ChildProfileRepository childProfileRepository) {
        this.achievementRepository = achievementRepository;
        this.childAchievementRepository = childAchievementRepository;
        this.childProfileRepository = childProfileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AchievementResponse> getAllAchievements() {
        return achievementRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AchievementResponse getAchievementById(Long id) {
        return toResponse(findAchievement(id));
    }

    @Override
    @Transactional
    public AchievementResponse createAchievement(AchievementRequest request) {
        Achievement achievement = Achievement.builder()
                .name(request.name().trim())
                .description(normalize(request.description()))
                .icon(normalize(request.icon()))
                .conditionType(normalize(request.conditionType()))
                .conditionValue(request.conditionValue())
                .conditionJson(normalize(request.conditionJson()))
                .rewardCoin(valueOrDefault(request.rewardCoin(), 0))
                .rewardXp(valueOrDefault(request.rewardXp(), 0))
                .rewardType(request.rewardType())
                .rewardValue(normalize(request.rewardValue()))
                .build();

        return toResponse(achievementRepository.save(achievement));
    }

    @Override
    @Transactional
    public AchievementResponse updateAchievement(Long id, AchievementRequest request) {
        Achievement achievement = findAchievement(id);
        achievement.setName(request.name().trim());
        achievement.setDescription(normalize(request.description()));
        achievement.setIcon(normalize(request.icon()));
        achievement.setConditionType(normalize(request.conditionType()));
        achievement.setConditionValue(request.conditionValue());
        achievement.setConditionJson(normalize(request.conditionJson()));
        achievement.setRewardCoin(valueOrDefault(request.rewardCoin(), 0));
        achievement.setRewardXp(valueOrDefault(request.rewardXp(), 0));
        achievement.setRewardType(request.rewardType());
        achievement.setRewardValue(normalize(request.rewardValue()));

        return toResponse(achievementRepository.save(achievement));
    }

    @Override
    @Transactional
    public void deleteAchievement(Long id) {
        achievementRepository.delete(findAchievement(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildAchievementResponse> getChildAchievements(Long childId) {
        return childAchievementRepository.findByChildId(childId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChildAchievementResponse getChildAchievementById(Long id) {
        return toResponse(findChildAchievement(id));
    }

    @Override
    @Transactional
    public ChildAchievementResponse createChildAchievement(ChildAchievementRequest request) {
        ChildAchievement childAchievement = ChildAchievement.builder()
                .child(findChild(request.childId()))
                .achievement(findAchievement(request.achievementId()))
                .progressValue(valueOrDefault(request.progressValue(), 0))
                .earnedAt(request.earnedAt())
                .build();

        return toResponse(childAchievementRepository.save(childAchievement));
    }

    @Override
    @Transactional
    public ChildAchievementResponse updateChildAchievement(Long id, ChildAchievementRequest request) {
        ChildAchievement childAchievement = findChildAchievement(id);
        applyChildAchievementRequest(childAchievement, request);
        return toResponse(childAchievementRepository.save(childAchievement));
    }

    @Override
    @Transactional
    public ChildAchievementResponse upsertChildAchievement(ChildAchievementRequest request) {
        ChildAchievement childAchievement = childAchievementRepository
                .findByChildIdAndAchievementId(request.childId(), request.achievementId())
                .orElseGet(ChildAchievement::new);
        applyChildAchievementRequest(childAchievement, request);
        return toResponse(childAchievementRepository.save(childAchievement));
    }

    @Override
    @Transactional
    public void deleteChildAchievement(Long id) {
        childAchievementRepository.delete(findChildAchievement(id));
    }

    private void applyChildAchievementRequest(ChildAchievement childAchievement, ChildAchievementRequest request) {
        childAchievement.setChild(findChild(request.childId()));
        childAchievement.setAchievement(findAchievement(request.achievementId()));
        childAchievement.setProgressValue(valueOrDefault(request.progressValue(), 0));
        childAchievement.setEarnedAt(request.earnedAt());
    }

    private Achievement findAchievement(Long id) {
        return achievementRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ACHIEVEMENT_NOT_FOUND));
    }

    private ChildAchievement findChildAchievement(Long id) {
        return childAchievementRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_ACHIEVEMENT_NOT_FOUND));
    }

    private ChildProfile findChild(Long id) {
        return childProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
    }

    private AchievementResponse toResponse(Achievement achievement) {
        return new AchievementResponse(
                achievement.getId(),
                achievement.getName(),
                achievement.getDescription(),
                achievement.getIcon(),
                achievement.getConditionType(),
                achievement.getConditionValue(),
                achievement.getConditionJson(),
                achievement.getRewardCoin(),
                achievement.getRewardXp(),
                achievement.getRewardType(),
                achievement.getRewardValue(),
                achievement.getCreatedAt(),
                achievement.getUpdatedAt()
        );
    }

    private ChildAchievementResponse toResponse(ChildAchievement childAchievement) {
        Achievement achievement = childAchievement.getAchievement();
        return new ChildAchievementResponse(
                childAchievement.getId(),
                childAchievement.getChild().getId(),
                achievement.getId(),
                achievement.getName(),
                achievement.getIcon(),
                childAchievement.getProgressValue(),
                childAchievement.getEarnedAt(),
                childAchievement.getCreatedAt(),
                childAchievement.getUpdatedAt()
        );
    }

    private <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
