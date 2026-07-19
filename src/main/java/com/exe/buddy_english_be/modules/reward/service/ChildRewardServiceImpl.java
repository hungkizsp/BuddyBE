package com.exe.buddy_english_be.modules.reward.service;

import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.reward.dto.CreateChildRewardRequest;
import com.exe.buddy_english_be.modules.reward.dto.ChildRewardResponse;
import com.exe.buddy_english_be.modules.reward.dto.UpdateChildRewardRequest;
import com.exe.buddy_english_be.modules.reward.entity.ChildReward;
import com.exe.buddy_english_be.modules.reward.entity.Reward;
import com.exe.buddy_english_be.modules.reward.repository.ChildRewardRepository;
import com.exe.buddy_english_be.modules.reward.repository.RewardRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChildRewardServiceImpl implements ChildRewardService {

    private final ChildRewardRepository childRewardRepository;
    private final ChildProfileRepository childProfileRepository;
    private final RewardRepository rewardRepository;

    @Override
    @Transactional
    public ChildRewardResponse createChildReward(CreateChildRewardRequest request) {
        ChildProfile child = childProfileRepository.findById(request.childId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
        
        Reward reward = rewardRepository.findById(request.rewardId())
                .orElseThrow(() -> new BusinessException(ErrorCode.REWARD_NOT_FOUND));

        ChildReward childReward = ChildReward.builder()
                .child(child)
                .reward(reward)
                .equipped(request.equipped() != null ? request.equipped() : false)
                .obtainedAt(request.obtainedAt())
                .build();
        ChildReward saved = childRewardRepository.save(childReward);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ChildRewardResponse getChildRewardById(Long id) {
        ChildReward childReward = childRewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_REWARD_NOT_FOUND));
        return mapToResponse(childReward);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildRewardResponse> getAllChildRewards() {
        return childRewardRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChildRewardResponse updateChildReward(Long id, UpdateChildRewardRequest request) {
        ChildReward childReward = childRewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_REWARD_NOT_FOUND));

        if (request.equipped() != null) childReward.setEquipped(request.equipped());
        if (request.obtainedAt() != null) childReward.setObtainedAt(request.obtainedAt());

        ChildReward updated = childRewardRepository.save(childReward);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteChildReward(Long id) {
        if (!childRewardRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.CHILD_REWARD_NOT_FOUND);
        }
        childRewardRepository.deleteById(id);
    }

    private ChildRewardResponse mapToResponse(ChildReward childReward) {
        return ChildRewardResponse.builder()
                .id(childReward.getId())
                .childId(childReward.getChild().getId())
                .rewardId(childReward.getReward().getId())
                .equipped(childReward.getEquipped())
                .obtainedAt(childReward.getObtainedAt())
                .build();
    }
}
