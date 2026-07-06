package com.exe.buddy_english_be.modules.reward.service;

import com.exe.buddy_english_be.modules.reward.dto.CreateRewardRequest;
import com.exe.buddy_english_be.modules.reward.dto.RewardResponse;
import com.exe.buddy_english_be.modules.reward.dto.UpdateRewardRequest;

import java.util.List;

public interface RewardService {
    RewardResponse createReward(CreateRewardRequest request);
    RewardResponse getRewardById(Long id);
    List<RewardResponse> getAllRewards();
    RewardResponse updateReward(Long id, UpdateRewardRequest request);
    void deleteReward(Long id);
}
