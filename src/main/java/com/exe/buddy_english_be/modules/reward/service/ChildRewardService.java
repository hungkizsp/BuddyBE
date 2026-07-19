package com.exe.buddy_english_be.modules.reward.service;

import com.exe.buddy_english_be.modules.reward.dto.CreateChildRewardRequest;
import com.exe.buddy_english_be.modules.reward.dto.ChildRewardResponse;
import com.exe.buddy_english_be.modules.reward.dto.UpdateChildRewardRequest;

import java.util.List;

public interface ChildRewardService {
    ChildRewardResponse createChildReward(CreateChildRewardRequest request);
    ChildRewardResponse getChildRewardById(Long id);
    List<ChildRewardResponse> getAllChildRewards();
    ChildRewardResponse updateChildReward(Long id, UpdateChildRewardRequest request);
    void deleteChildReward(Long id);
}
