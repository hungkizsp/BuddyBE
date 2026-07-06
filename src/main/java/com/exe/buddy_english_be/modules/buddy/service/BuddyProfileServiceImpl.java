package com.exe.buddy_english_be.modules.buddy.service;

import com.exe.buddy_english_be.modules.buddy.dto.BuddyProfileResponse;
import com.exe.buddy_english_be.modules.buddy.dto.CreateBuddyProfileRequest;
import com.exe.buddy_english_be.modules.buddy.dto.UpdateBuddyProfileRequest;
import com.exe.buddy_english_be.modules.buddy.entity.BuddyProfile;
import com.exe.buddy_english_be.modules.buddy.enums.BuddyMood;
import com.exe.buddy_english_be.modules.buddy.repository.BuddyProfileRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuddyProfileServiceImpl implements BuddyProfileService {

    private final BuddyProfileRepository buddyProfileRepository;
    private final ChildProfileRepository childProfileRepository;

    @Override
    @Transactional
    public BuddyProfileResponse createBuddyProfile(CreateBuddyProfileRequest request) {
        ChildProfile child = childProfileRepository.findById(request.childId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));

        BuddyProfile buddyProfile = BuddyProfile.builder()
                .child(child)
                .name(request.name() != null ? request.name() : "Buddy")
                .level(request.level() != null ? request.level() : 1)
                .friendshipPoints(request.friendshipPoints() != null ? request.friendshipPoints() : 0)
                .mood(request.mood() != null ? request.mood() : BuddyMood.HAPPY)
                .energy(request.energy() != null ? request.energy() : 100)
                .lastInteractionAt(request.lastInteractionAt())
                .build();
        
        BuddyProfile saved = buddyProfileRepository.save(buddyProfile);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BuddyProfileResponse getBuddyProfileById(Long id) {
        BuddyProfile buddyProfile = buddyProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BUDDY_PROFILE_NOT_FOUND));
        return mapToResponse(buddyProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuddyProfileResponse> getAllBuddyProfiles() {
        return buddyProfileRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BuddyProfileResponse updateBuddyProfile(Long id, UpdateBuddyProfileRequest request) {
        BuddyProfile buddyProfile = buddyProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BUDDY_PROFILE_NOT_FOUND));

        if (request.name() != null) buddyProfile.setName(request.name());
        if (request.level() != null) buddyProfile.setLevel(request.level());
        if (request.friendshipPoints() != null) buddyProfile.setFriendshipPoints(request.friendshipPoints());
        if (request.mood() != null) buddyProfile.setMood(request.mood());
        if (request.energy() != null) buddyProfile.setEnergy(request.energy());
        if (request.lastInteractionAt() != null) buddyProfile.setLastInteractionAt(request.lastInteractionAt());

        BuddyProfile updated = buddyProfileRepository.save(buddyProfile);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public void deleteBuddyProfile(Long id) {
        if (!buddyProfileRepository.existsById(id)) {
            throw new BusinessException(ErrorCode.BUDDY_PROFILE_NOT_FOUND);
        }
        buddyProfileRepository.deleteById(id);
    }

    private BuddyProfileResponse mapToResponse(BuddyProfile buddyProfile) {
        return BuddyProfileResponse.builder()
                .id(buddyProfile.getId())
                .childId(buddyProfile.getChild().getId())
                .name(buddyProfile.getName())
                .level(buddyProfile.getLevel())
                .friendshipPoints(buddyProfile.getFriendshipPoints())
                .mood(buddyProfile.getMood())
                .energy(buddyProfile.getEnergy())
                .lastInteractionAt(buddyProfile.getLastInteractionAt())
                .build();
    }
}
