package com.exe.buddy_english_be.modules.reward.service;

import com.exe.buddy_english_be.modules.reward.dto.CreateRewardRequest;
import com.exe.buddy_english_be.modules.reward.dto.RewardResponse;
import com.exe.buddy_english_be.modules.reward.dto.UpdateRewardRequest;
import com.exe.buddy_english_be.modules.reward.entity.Reward;
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
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;

    @Override
    @Transactional
    public RewardResponse createReward(CreateRewardRequest request) {
        Reward reward = Reward.builder()
                .name(request.name())
                .type(request.type())
                .imageUrl(request.imageUrl())
                .price(request.price() != null ? request.price() : 0)
                .rarity(request.rarity())
                .isLimited(request.isLimited() != null ? request.isLimited() : false)
                .availableFrom(request.availableFrom())
                .availableTo(request.availableTo())
                .build();
        Reward savedReward = rewardRepository.save(reward);
        return mapToResponse(savedReward);
    }

    @Override
    @Transactional(readOnly = true)
    public RewardResponse getRewardById(Long id) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.REWARD_NOT_FOUND));
        return mapToResponse(reward);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RewardResponse> getAllRewards() {
        return rewardRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RewardResponse updateReward(Long id, UpdateRewardRequest request) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.REWARD_NOT_FOUND));

        if (request.name() != null) reward.setName(request.name());
        if (request.type() != null) reward.setType(request.type());
        if (request.imageUrl() != null) reward.setImageUrl(request.imageUrl());
        if (request.price() != null) reward.setPrice(request.price());
        if (request.rarity() != null) reward.setRarity(request.rarity());
        if (request.isLimited() != null) reward.setIsLimited(request.isLimited());
        if (request.availableFrom() != null) reward.setAvailableFrom(request.availableFrom());
        if (request.availableTo() != null) reward.setAvailableTo(request.availableTo());

        Reward updatedReward = rewardRepository.save(reward);
        return mapToResponse(updatedReward);
    }

    @Override
    @Transactional
    public void deleteReward(Long id) {
        if (!rewardRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.REWARD_NOT_FOUND);
        }
        rewardRepository.deleteById(id);
    }

    private RewardResponse mapToResponse(Reward reward) {
        return RewardResponse.builder()
                .id(reward.getId())
                .name(reward.getName())
                .type(reward.getType())
                .imageUrl(reward.getImageUrl())
                .price(reward.getPrice())
                .rarity(reward.getRarity())
                .isLimited(reward.getIsLimited())
                .availableFrom(reward.getAvailableFrom())
                .availableTo(reward.getAvailableTo())
                .build();
    }
}
