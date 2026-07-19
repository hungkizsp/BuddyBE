package com.exe.buddy_english_be.modules.profile.service;

import com.exe.buddy_english_be.modules.profile.dto.ChildProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateChildProfileRequest;
import com.exe.buddy_english_be.modules.profile.dto.ParentChildrenResponse;
import com.exe.buddy_english_be.modules.profile.dto.ParentProfileResponse;
import com.exe.buddy_english_be.modules.profile.dto.UpdateParentProfileRequest;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.entity.ParentChild;
import com.exe.buddy_english_be.modules.profile.entity.ParentProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.modules.profile.repository.ParentChildRepository;
import com.exe.buddy_english_be.modules.profile.repository.ParentProfileRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ChildProfileRepository childProfileRepository;
    private final ParentProfileRepository parentProfileRepository;
    private final ParentChildRepository parentChildRepository;

    // ─── Child ───────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ChildProfileResponse getChildProfile(Long userId) {
        log.info("Fetching child profile for userId={}", userId);
        ChildProfile child = childProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return mapToChildResponse(child);
    }

    @Override
    @Transactional
    public ChildProfileResponse updateChildProfile(Long userId, UpdateChildProfileRequest request) {
        log.info("Updating child profile for userId={}", userId);
        ChildProfile child = childProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (request.nickname() != null) child.setNickname(request.nickname());
        if (request.avatarUrl() != null) child.setAvatarUrl(request.avatarUrl());
        if (request.birthDate() != null) child.setBirthDate(request.birthDate());
        if (request.gender() != null) child.setGender(request.gender());
        if (request.xp() != null) child.setXp(request.xp());
        if (request.coins() != null) child.setCoins(request.coins());
        if (request.activeCustomCharacterUrl() != null) child.setActiveCustomCharacterUrl(request.activeCustomCharacterUrl());

        childProfileRepository.save(child);
        return mapToChildResponse(child);
    }

    // ─── Parent ──────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ParentProfileResponse getParentProfile(Long userId) {
        log.info("Fetching parent profile for userId={}", userId);
        ParentProfile parent = parentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        return mapToParentResponse(parent);
    }

    @Override
    @Transactional
    public ParentProfileResponse updateParentProfile(Long userId, UpdateParentProfileRequest request) {
        log.info("Updating parent profile for userId={}", userId);
        ParentProfile parent = parentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (request.fullName() != null) parent.setFullName(request.fullName());
        if (request.phone() != null) parent.setPhone(request.phone());
        if (request.occupation() != null) parent.setOccupation(request.occupation());
        if (request.avatarUrl() != null) parent.setAvatarUrl(request.avatarUrl());

        parentProfileRepository.save(parent);
        return mapToParentResponse(parent);
    }

    // ─── Parent–Child ────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public ParentChildrenResponse getChildrenOfParent(Long parentUserId) {
        log.info("Fetching children for parentUserId={}", parentUserId);
        ParentProfile parent = parentProfileRepository.findByUserId(parentUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<ParentChildrenResponse.ChildSummary> children = parentChildRepository
                .findByParentId(parent.getId())
                .stream()
                .map(this::mapToChildSummary)
                .toList();

        return ParentChildrenResponse.builder()
                .parentId(parent.getId())
                .children(children)
                .build();
    }

    // ─── Mappers ─────────────────────────────────────────────────────────────

    private ChildProfileResponse mapToChildResponse(ChildProfile child) {
        return ChildProfileResponse.builder()
                .id(child.getId())
                .userId(child.getUser() != null ? child.getUser().getId() : null)
                .nickname(child.getNickname())
                .avatarUrl(child.getAvatarUrl())
                .birthDate(child.getBirthDate())
                .gender(child.getGender())
                .level(child.getLevel())
                .xp(child.getXp())
                .coins(child.getCoins())
                .streakDays(child.getStreakDays())
                .lastLoginDate(child.getLastLoginDate())
                .lastSessionAt(child.getLastSessionAt())
                .activeCustomCharacterUrl(child.getActiveCustomCharacterUrl())
                .build();
    }

    private ParentProfileResponse mapToParentResponse(ParentProfile parent) {
        return ParentProfileResponse.builder()
                .id(parent.getId())
                .userId(parent.getUser() != null ? parent.getUser().getId() : null)
                .fullName(parent.getFullName())
                .phone(parent.getPhone())
                .occupation(parent.getOccupation())
                .avatarUrl(parent.getAvatarUrl())
                .build();
    }

    private ParentChildrenResponse.ChildSummary mapToChildSummary(ParentChild pc) {
        ChildProfile child = pc.getChild();
        return ParentChildrenResponse.ChildSummary.builder()
                .id(child.getId())
                .childUserId(child.getUser() != null ? child.getUser().getId() : null)
                .nickname(child.getNickname())
                .avatarUrl(child.getAvatarUrl())
                .level(child.getLevel())
                .xp(child.getXp())
                .relationship(pc.getRelationship())
                .build();
    }
}
